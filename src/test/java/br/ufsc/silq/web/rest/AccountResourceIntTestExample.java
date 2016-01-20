package br.ufsc.silq.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.ufsc.silq.Application;
import br.ufsc.silq.domain.Authority;
import br.ufsc.silq.domain.User;
import br.ufsc.silq.repository.AuthorityRepository;
import br.ufsc.silq.repository.UserRepository;
import br.ufsc.silq.security.AuthoritiesConstants;
import br.ufsc.silq.service.MailService;
import br.ufsc.silq.service.UserService;
import br.ufsc.silq.web.rest.dto.UserDTO;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountResourceIntTestExample {

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	@Inject
	private UserService userService;

	@Mock
	private UserService mockUserService;

	@Mock
	private MailService mockMailService;

	private MockMvc restUserMockMvc;

	private MockMvc restMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		doNothing().when(this.mockMailService).sendActivationEmail((User) anyObject(), anyString());

		AccountResource accountResource = new AccountResource();
		ReflectionTestUtils.setField(accountResource, "userRepository", this.userRepository);
		ReflectionTestUtils.setField(accountResource, "userService", this.userService);
		ReflectionTestUtils.setField(accountResource, "mailService", this.mockMailService);

		AccountResource accountUserMockResource = new AccountResource();
		ReflectionTestUtils.setField(accountUserMockResource, "userRepository", this.userRepository);
		ReflectionTestUtils.setField(accountUserMockResource, "userService", this.mockUserService);
		ReflectionTestUtils.setField(accountUserMockResource, "mailService", this.mockMailService);

		this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
	}

	@Test
	public void testNonAuthenticatedUser() throws Exception {
		this.restUserMockMvc.perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void testAuthenticatedUser() throws Exception {
		this.restUserMockMvc.perform(get("/api/authenticate").with(request -> {
			request.setRemoteUser("test");
			return request;
		}).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("test"));
	}

	@Test
	public void testGetExistingAccount() throws Exception {
		Set<Authority> authorities = new HashSet<>();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.ADMIN);
		authorities.add(authority);

		User user = new User();
		user.setLogin("test");
		user.setFirstName("john");
		user.setLastName("doe");
		user.setEmail("john.doe@jhipter.com");
		user.setAuthorities(authorities);
		when(this.mockUserService.getUserWithAuthorities()).thenReturn(user);

		this.restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.firstName").value("john"))
				.andExpect(jsonPath("$.lastName").value("doe"))
				.andExpect(jsonPath("$.email").value("john.doe@jhipter.com"))
				.andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
	}

	@Test
	public void testGetUnknownAccount() throws Exception {
		when(this.mockUserService.getUserWithAuthorities()).thenReturn(null);

		this.restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	@Transactional
	public void testRegisterValid() throws Exception {
		UserDTO u = new UserDTO("joe", // login
				"password", // password
				"Joe", // firstName
				"Shmoe", // lastName
				"joe@example.com", // e-mail
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

		Optional<User> user = this.userRepository.findOneByLogin("joe");
		assertThat(user.isPresent()).isTrue();
	}

	@Test
	@Transactional
	public void testRegisterInvalidLogin() throws Exception {
		UserDTO u = new UserDTO("funky-log!n", // login <-- invalid
				"password", // password
				"Funky", // firstName
				"One", // lastName
				"funky@example.com", // e-mail
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

		this.restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isBadRequest());

		Optional<User> user = this.userRepository.findOneByEmail("funky@example.com");
		assertThat(user.isPresent()).isFalse();
	}

	@Test
	@Transactional
	public void testRegisterInvalidEmail() throws Exception {
		UserDTO u = new UserDTO("bob", // login
				"password", // password
				"Bob", // firstName
				"Green", // lastName
				"invalid", // e-mail <-- invalid
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

		this.restUserMockMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isBadRequest());

		Optional<User> user = this.userRepository.findOneByLogin("bob");
		assertThat(user.isPresent()).isFalse();
	}

	@Test
	@Transactional
	public void testRegisterDuplicateLogin() throws Exception {
		// Good
		UserDTO u = new UserDTO("alice", // login
				"password", // password
				"Alice", // firstName
				"Something", // lastName
				"alice@example.com", // e-mail
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

		// Duplicate login, different e-mail
		UserDTO dup = new UserDTO(u.getLogin(), u.getPassword(), u.getLogin(), u.getLastName(), "alicejr@example.com",
				true, u.getLangKey(), u.getAuthorities());

		// Good user
		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

		// Duplicate login
		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dup))).andExpect(status().is4xxClientError());

		Optional<User> userDup = this.userRepository.findOneByEmail("alicejr@example.com");
		assertThat(userDup.isPresent()).isFalse();
	}

	@Test
	@Transactional
	public void testRegisterDuplicateEmail() throws Exception {
		// Good
		UserDTO u = new UserDTO("john", // login
				"password", // password
				"John", // firstName
				"Doe", // lastName
				"john@example.com", // e-mail
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

		// Duplicate e-mail, different login
		UserDTO dup = new UserDTO("johnjr", u.getPassword(), u.getLogin(), u.getLastName(), u.getEmail(), true,
				u.getLangKey(), u.getAuthorities());

		// Good user
		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

		// Duplicate e-mail
		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dup))).andExpect(status().is4xxClientError());

		Optional<User> userDup = this.userRepository.findOneByLogin("johnjr");
		assertThat(userDup.isPresent()).isFalse();
	}

	@Test
	@Transactional
	public void testRegisterAdminIsIgnored() throws Exception {
		UserDTO u = new UserDTO("badguy", // login
				"password", // password
				"Bad", // firstName
				"Guy", // lastName
				"badguy@example.com", // e-mail
				true, // activated
				"en", // langKey
				new HashSet<>(Arrays.asList(AuthoritiesConstants.ADMIN)) // <--
																			// only
																			// admin
																			// should
																			// be
																			// able
																			// to
																			// do
																			// that
		);

		this.restMvc.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

		Optional<User> userDup = this.userRepository.findOneByLogin("badguy");
		assertThat(userDup.isPresent()).isTrue();
		assertThat(userDup.get().getAuthorities()).hasSize(1)
				.containsExactly(this.authorityRepository.findOne(AuthoritiesConstants.USER));
	}
}
