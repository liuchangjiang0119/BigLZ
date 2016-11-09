package com.starsutdio.biglz.model.bean;

/**
 * Created by windfall on 16-11-2.
 */

public class LoginBean {
    /**
     * status : 200
     * msg : {"un":"abcdefg","uid":".cy6zl","acckey":"25fbd691a77a8ce04n9b86a66e128513e2bp"}
     */

    private int status;
    /**
     * un : abcdefg
     * uid : .cy6zl
     * acckey : 25fbd691a77a8ce04n9b86a66e128513e2bp
     */

    private MsgBean msg;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }


    public static class MsgBean {
        private String un;
        private String uid;
        private String acckey;

        public String getUn() {
            return un;
        }

        public void setUn(String un) {
            this.un = un;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAcckey() {
            return acckey;
        }

        public void setAcckey(String acckey) {
            this.acckey = acckey;
        }
    }
}
