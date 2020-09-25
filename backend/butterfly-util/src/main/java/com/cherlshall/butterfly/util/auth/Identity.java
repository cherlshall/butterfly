package com.cherlshall.butterfly.util.auth;

/**
 * 所有身份
 * Created by htf on 2020/9/24.
 */
public enum Identity {

    VISITOR,
    NCA_USER(VISITOR),
    NCA_ADMIN(NCA_USER),
    NDDS_USER(VISITOR),
    NDDS_ADMIN(NDDS_USER),
    SUPER(NCA_ADMIN, NDDS_ADMIN);

    /**
     * 拥有哪些身份的权限
     */
    private final Identity[] contains;

    public static final String SEP = "&";

    Identity(Identity... contains) {
        this.contains = contains;
    }

    /**
     * 身份 userAuth 是否拥有本身份
     * @param userAuth 用户身份
     * @return true: 是
     */
    public boolean permit(String userAuth) {
        if (userAuth == null) {
            return false;
        }
        Identity userIdentity = Identity.valueOf(userAuth.toUpperCase());
        return permit(userIdentity);
    }

    private boolean permit(Identity userIdentity) {
        if (userIdentity == null) {
            return false;
        }
        if (userIdentity == this) {
            return true;
        }
        if (userIdentity.contains != null) {
            for (Identity contain : userIdentity.contains) {
                if (permit(contain)) {
                    return true;
                }
            }
        }
        return false;
    }

}
