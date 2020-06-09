package cn.com.tiza.constant;

/**
 * 权限定义
 *
 * @author tiza
 */
public final class Permissions {
    private Permissions() {
    }

    public static final class TerminalInfo {
        private TerminalInfo() {
        }

        public static final class Query {
            public static final int VALUE = 10451010;
            public static final String DESCRIPTION = "终端信息-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10451020;
            public static final String DESCRIPTION = "终端信息-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10451030;
            public static final String DESCRIPTION = "终端信息-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10451040;
            public static final String DESCRIPTION = "终端信息-删除";

            private Delete() {
            }
        }

        public static final class Import {
            public static final int VALUE = 10451050;
            public static final String DESCRIPTION = "终端信息-导入";

            private Import() {
            }
        }

        public static final class Export {
            public static final int VALUE = 10451060;
            public static final String DESCRIPTION = "终端信息-导出";

            private Export() {
            }
        }
    }

    /**
     * 用户管理
     */
    public static final class User {
        private User() {
        }

        public static final class Query {
            public static final int VALUE = 10901010;
            public static final String DESCRIPTION = "用户管理-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10901011;
            public static final String DESCRIPTION = "用户管理-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10901012;
            public static final String DESCRIPTION = "用户管理-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10901013;
            public static final String DESCRIPTION = "用户管理-删除";

            private Delete() {
            }
        }

        public static final class ResetPwd {
            public static final int VALUE = 10901020;
            public static final String DESCRIPTION = "用户管理-重置密码";

            private ResetPwd() {
            }
        }

        public static final class Export {
            public static final int VALUE = 10901021;
            public static final String DESCRIPTION = "用户管理-导出";

            private Export() {
            }
        }
    }

    /**
     * 角色管理
     */
    public static final class Role {
        private Role() {
        }

        public static final class Query {
            public static final int VALUE = 10902010;
            public static final String DESCRIPTION = "角色管理-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10902011;
            public static final String DESCRIPTION = "角色管理-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10902012;
            public static final String DESCRIPTION = "角色管理-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10902013;
            public static final String DESCRIPTION = "角色管理-删除";

            private Delete() {
            }
        }

        public static final class Auth {
            public static final int VALUE = 10902014;
            public static final String DESCRIPTION = "角色管理-授权";

            private Auth() {
            }
        }
    }

    /**
     * 机构管理
     */
    public static final class Organization {
        private Organization() {
        }

        public static final class Query {
            public static final int VALUE = 10903010;
            public static final String DESCRIPTION = "机构管理-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10903011;
            public static final String DESCRIPTION = "机构管理-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10903012;
            public static final String DESCRIPTION = "机构管理-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10903013;
            public static final String DESCRIPTION = "机构管理-删除";

            private Delete() {
            }
        }
    }

    /**
     * 融资机构管理
     */
    public static final class Finance {
        private Finance() {
        }

        public static final class Query {
            public static final int VALUE = 10904010;
            public static final String DESCRIPTION = "融资机构管理-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10904011;
            public static final String DESCRIPTION = "融资机构管理-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10904012;
            public static final String DESCRIPTION = "融资机构管理-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10904013;
            public static final String DESCRIPTION = "融资机构管理-删除";

            private Delete() {
            }
        }
    }

    /**
     * 客户管理
     */
    public static final class Customer {
        private Customer() {
        }

        public static final class Query {
            public static final int VALUE = 10905010;
            public static final String DESCRIPTION = "客户管理-查询";

            private Query() {
            }
        }

        public static final class Create {
            public static final int VALUE = 10905011;
            public static final String DESCRIPTION = "客户管理-新增";

            private Create() {
            }
        }

        public static final class Update {
            public static final int VALUE = 10905012;
            public static final String DESCRIPTION = "客户管理-修改";

            private Update() {
            }
        }

        public static final class Delete {
            public static final int VALUE = 10905013;
            public static final String DESCRIPTION = "客户管理-删除";

            private Delete() {
            }
        }
    }

    /**
     * 系统日志
     */
    public static final class SystemLog {
        private SystemLog() {
        }
    }
}