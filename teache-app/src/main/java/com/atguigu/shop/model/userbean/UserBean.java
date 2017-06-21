package com.atguigu.shop.model.userbean;

/**
 * Created by shkstart on 2017/1/16 0016.
 */

public class UserBean {

    /**
     * exception : null
     * message : 请求成功
     * body : {"user":{"imgurl":null,"password":"er","createTimeString":"","updateTimeString":"","createTime":null,"phone":null,"updateTime":null,"id":1,"enableFlag":0,"username":"edre"}}
     * status : 200
     * timestamp : 1484555478684
     */
    private String exception;
    private String message;
    private BodyEntity body;
    private int status;
    private String timestamp;

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBody(BodyEntity body) {
        this.body = body;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public BodyEntity getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public class BodyEntity {
        /**
         * user : {"imgurl":null,"password":"er","createTimeString":"","updateTimeString":"","createTime":null,"phone":null,"updateTime":null,"id":1,"enableFlag":0,"username":"edre"}
         */
        private UserEntity user;

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public UserEntity getUser() {
            return user;
        }

        public class UserEntity {
            /**
             * imgurl : null
             * password : er
             * createTimeString :
             * updateTimeString :
             * createTime : null
             * phone : null
             * updateTime : null
             * id : 1
             * enableFlag : 0
             * username : edre
             */
            private String imgurl;
            private String password;
            private String createTimeString;
            private String updateTimeString;
            private String createTime;
            private String phone;
            private String updateTime;
            private int id;
            private int enableFlag;
            private String username;

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setCreateTimeString(String createTimeString) {
                this.createTimeString = createTimeString;
            }

            public void setUpdateTimeString(String updateTimeString) {
                this.updateTimeString = updateTimeString;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setEnableFlag(int enableFlag) {
                this.enableFlag = enableFlag;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getImgurl() {
                return imgurl;
            }

            public String getPassword() {
                return password;
            }

            public String getCreateTimeString() {
                return createTimeString;
            }

            public String getUpdateTimeString() {
                return updateTimeString;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getPhone() {
                return phone;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public int getId() {
                return id;
            }

            public int getEnableFlag() {
                return enableFlag;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}
