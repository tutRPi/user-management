package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.Role;
import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.ConfirmationTokenService;
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


    public void loadDbData(boolean twoFaEnabled) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Role role = new Role();
        role.setCreatedOn(new Date());
        role.setName(SecurityRole.ROLE_USER.getName());

        user = new User();
        user.setEmail(UUID.randomUUID().toString() + "@example.com");
        user.setPassword(this.passwordEncoder.encode("Passw0rd!123"));
        user.setFirstName("First");
        user.setLastName("Last");
        user.setTwoFaEnabled(twoFaEnabled);
        user.setCreatedOn(new Date());

        RoleByUser roleByUser = new RoleByUser();
        roleByUser.setRole(role);
        roleByUser.setUser(user);
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        user.setRolesByUserCollection(rolesByUser);


        token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);
        confirmationToken.setCreatedOn(new Date());
        confirmationToken.setExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));

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
