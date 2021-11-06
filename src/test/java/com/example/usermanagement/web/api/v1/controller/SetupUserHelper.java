package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.Role;
import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Transactional
public class SetupUserHelper {

    @Autowired
    protected MockMvc mockMvc;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @MockBean
    ConfirmationTokenService confirmationTokenService;

    protected User user;
    protected String token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Role role = new Role();
        role.setDtCreatedOn(new Date());
        role.setDsName(SecurityRole.ROLE_USER.getName());
        entityManager.persist(role);

        user = new User();
        user.setDsEmail(UUID.randomUUID().toString() + "@example.com");
        user.setDsPassword(this.passwordEncoder.encode("Passw0rd!123"));
        user.setDsFirstName("First");
        user.setDsLastName("Last");
        user.setYn2faEnabled(true);
        user.setDtCreatedOn(new Date());

        RoleByUser roleByUser = new RoleByUser();
        roleByUser.setRole(role);
        roleByUser.setNmUserId(user);
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        user.setRolesByUserCollection(rolesByUser);


        token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setDsToken(token);
        confirmationToken.setNmUserId(user);
        confirmationToken.setDtCreatedOn(new Date());
        confirmationToken.setDtExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));

        List<ConfirmationToken> confirmationTokensByUser = new ArrayList<>();
        confirmationTokensByUser.add(confirmationToken);
        user.setConfirmationTokensByUserCollection(confirmationTokensByUser);

        entityManager.persist(user);

        /*
            flush managed entities to the database to populate identifier field
         */
        entityManager.flush();
//        entityManager.clear();
    }
}
