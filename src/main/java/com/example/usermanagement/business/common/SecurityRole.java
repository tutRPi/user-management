package com.example.usermanagement.business.common;

public enum SecurityRole {
    ROLE_2FA_CODE_VERIFICATION(-1, Names.ROLE_2FA_CODE_VERIFICATION),

    // Adjust roles as needed !
    ROLE_ADMIN(1, Names.ROLE_ADMIN),
    ROLE_USER(2, Names.ROLE_USER);

    SecurityRole(int id, String name) {
        this.id = id;
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
