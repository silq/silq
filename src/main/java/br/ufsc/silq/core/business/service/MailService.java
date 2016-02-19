package br.ufsc.silq.core.business.service;

import java.util.Locale;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.CharEncoding;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import br.ufsc.silq.config.JHipsterProperties;
import br.ufsc.silq.core.business.entities.Usuario;
import br.ufsc.silq.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
@Slf4j
public class MailService {

	@Inject
	private JHipsterProperties jHipsterProperties;

	@Inject
	private JavaMailSenderImpl javaMailSender;

	@Inject
	private MessageSource messageSource;

	@Inject
	private SpringTemplateEngine templateEngine;

	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
				isHtml, to, subject, content);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
			message.setTo(to);
			message.setFrom(this.jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			message.setText(content, isHtml);
			this.javaMailSender.send(mimeMessage);
			log.debug("Sent e-mail to User '{}'", to);
		} catch (Exception e) {
			log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
		}
	}

	@Async
	public void sendActivationEmail(User user, String baseUrl) {
		log.debug("Sending activation e-mail to '{}'", user.getEmail());
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable("user", user);
		context.setVariable("baseUrl", baseUrl);
		String content = this.templateEngine.process("activationEmail", context);
		String subject = this.messageSource.getMessage("email.activation.title", null, locale);
		this.sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendCreationEmail(User user, String baseUrl) {
		log.debug("Sending creation e-mail to '{}'", user.getEmail());
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable("user", user);
		context.setVariable("baseUrl", baseUrl);
		String content = this.templateEngine.process("creationEmail", context);
		String subject = this.messageSource.getMessage("email.activation.title", null, locale);
		this.sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendPasswordResetMail(Usuario usuario, String baseUrl) {
		log.debug("Sending password reset e-mail to '{}'", usuario.getEmail());
		Locale locale = Locale.forLanguageTag("pt-br");
		Context context = new Context(locale);
		context.setVariable("user", usuario);
		context.setVariable("baseUrl", baseUrl);
		String content = this.templateEngine.process("passwordResetEmail", context);
		String subject = this.messageSource.getMessage("email.reset.title", null, locale);
		this.sendEmail(usuario.getEmail(), subject, content, false, true);
	}

}
