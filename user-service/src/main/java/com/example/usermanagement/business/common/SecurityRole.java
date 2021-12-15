package com.example.usermanagement.business.common;

public enum SecurityRole {
    ROLE_2FA_CODE_VERIFICATION(Names.ROLE_2FA_CODE_VERIFICATION),

    // Adjust roles as needed !
    ROLE_ADMIN(Names.ROLE_ADMIN),
    ROLE_USER(Names.ROLE_USER);

    SecurityRole(String name) {
        this.name = name;
    }

    private int id;
    private String name;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static class Names {
        private static final String ROLE_PREFIX = "ROLE_";

        public static final String ROLE_2FA_CODE_VERIFICATION = ROLE_PREFIX + "2FA_CODE_VERIFICATION";
        public static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
        public static final String ROLE_USER = ROLE_PREFIX + "USER";
    }
}
