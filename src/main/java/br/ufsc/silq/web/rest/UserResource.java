package br.ufsc.silq.web.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufsc.silq.core.business.service.MailService;
import br.ufsc.silq.core.business.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing users.
 *
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of
 * authorities.
 * </p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no DTO, a lot less code, and an outer-join which would be good for
 * performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </p>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 * </p>
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UserResource {

	@Inject
	private MailService mailService;

	@Inject
	private UsuarioService usuarioService;

	// /**
	// * POST /users -> Creates a new user.
	// * <p>
	// * Creates a new user if the login and email are not already used, and
	// sends
	// * an mail with an activation link. The user needs to be activated on
	// * creation.
	// * </p>
	// */
	// @RequestMapping(value = "/users", method = RequestMethod.POST, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// @Secured(AuthoritiesConstants.ADMIN)
	// public ResponseEntity<?> createUser(@RequestBody ManagedUserDTO
	// managedUserDTO, HttpServletRequest request)
	// throws URISyntaxException {
	// this.log.debug("REST request to save User : {}", managedUserDTO);
	// if
	// (this.usuarioService.findOneByLogin(managedUserDTO.getLogin()).isPresent())
	// {
	// return ResponseEntity.badRequest()
	// .headers(HeaderUtil.createFailureAlert("user-management", "userexists",
	// "Login already in use"))
	// .body(null);
	// } else if
	// (this.usuarioService.findOneByEmail(managedUserDTO.getEmail()).isPresent())
	// {
	// return ResponseEntity.badRequest()
	// .headers(HeaderUtil.createFailureAlert("user-management", "emailexists",
	// "Email already in use"))
	// .body(null);
	// } else {
	// User newUser = this.usuarioService.createUser(managedUserDTO);
	// String baseUrl = request.getScheme() + // "http"
	// "://" + // "://"
	// request.getServerName() + // "myhost"
	// ":" + // ":"
	// request.getServerPort() + // "80"
	// request.getContextPath(); // "/myContextPath" or "" if
	// // deployed in root context
	// this.mailService.sendCreationEmail(newUser, baseUrl);
	// return ResponseEntity
	// .created(new URI("/api/users/" + newUser.getLogin())).headers(HeaderUtil
	// .createAlert("A user is created with identifier " + newUser.getLogin(),
	// newUser.getLogin()))
	// .body(newUser);
	// }
	// }
	//
	// /**
	// * PUT /users -> Updates an existing User.
	// */
	// @RequestMapping(value = "/users", method = RequestMethod.PUT, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// @Transactional
	// @Secured(AuthoritiesConstants.ADMIN)
	// public ResponseEntity<ManagedUserDTO> updateUser(@RequestBody
	// ManagedUserDTO managedUserDTO)
	// throws URISyntaxException {
	// this.log.debug("REST request to update User : {}", managedUserDTO);
	// Optional<User> existingUser =
	// this.usuarioService.findOneByEmail(managedUserDTO.getEmail());
	// if (existingUser.isPresent() &&
	// !existingUser.get().getId().equals(managedUserDTO.getId())) {
	// return ResponseEntity.badRequest()
	// .headers(HeaderUtil.createFailureAlert("user-management", "emailexists",
	// "E-mail already in use"))
	// .body(null);
	// }
	// existingUser =
	// this.usuarioService.findOneByLogin(managedUserDTO.getLogin());
	// if (existingUser.isPresent() &&
	// !existingUser.get().getId().equals(managedUserDTO.getId())) {
	// return ResponseEntity.badRequest()
	// .headers(HeaderUtil.createFailureAlert("user-management", "userexists",
	// "Login already in use"))
	// .body(null);
	// }
	// return this.usuarioService.findOneById(managedUserDTO.getId()).map(user
	// -> {
	// user.setLogin(managedUserDTO.getLogin());
	// user.setFirstName(managedUserDTO.getFirstName());
	// user.setLastName(managedUserDTO.getLastName());
	// user.setEmail(managedUserDTO.getEmail());
	// user.setActivated(managedUserDTO.isActivated());
	// user.setLangKey(managedUserDTO.getLangKey());
	// Set<Authority> authorities = user.getAuthorities();
	// authorities.clear();
	// managedUserDTO.getAuthorities().stream()
	// .forEach(authority ->
	// authorities.add(authorityRepository.findOne(authority)));
	// return ResponseEntity.ok()
	// .headers(HeaderUtil.createAlert("A user is updated with identifier " +
	// managedUserDTO.getLogin(),
	// managedUserDTO.getLogin()))
	// .body(new
	// ManagedUserDTO(userRepository.findOne(managedUserDTO.getId())));
	// }).orElseGet(() -> new
	// ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	//
	// }
	//
	// /**
	// * GET /users -> get all users.
	// */
	// @RequestMapping(value = "/users", method = RequestMethod.GET, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// @Transactional(readOnly = true)
	// public ResponseEntity<List<ManagedUserDTO>> getAllUsers(Pageable
	// pageable) throws URISyntaxException {
	// Page<User> page = this.usuarioService.findAll(pageable);
	// List<ManagedUserDTO> managedUserDTOs =
	// page.getContent().stream().map(user -> new ManagedUserDTO(user))
	// .collect(Collectors.toList());
	// HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
	// "/api/users");
	// return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
	// }
	//
	// /**
	// * GET /users/:login -> get the "login" user.
	// */
	// @RequestMapping(value = "/users/{login:[_'.@a-z0-9-]+}", method =
	// RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String login)
	// {
	// this.log.debug("REST request to get User : {}", login);
	// return
	// this.usuarioService.getUserWithAuthoritiesByLogin(login).map(ManagedUserDTO::new)
	// .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO,
	// HttpStatus.OK))
	// .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	// }
	//
	// /**
	// * DELETE USER :login -> delete the "login" User.
	// */
	// @RequestMapping(value = "/users/{login}", method = RequestMethod.DELETE,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// @Timed
	// @Secured(AuthoritiesConstants.ADMIN)
	// public ResponseEntity<Void> deleteUser(@PathVariable String login) {
	// this.log.debug("REST request to delete User: {}", login);
	// this.usuarioService.deleteUserInformation(login);
	// return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is
	// deleted with identifier " + login, login))
	// .build();
	// }
}
