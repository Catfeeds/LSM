package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/13
 */
public class QueryUserRakingReturn {


    private String result;
    private PdBean pd;
    private PrePdBean prePd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PdBean getPd() {
        return pd;
    }

    public void setPd(PdBean pd) {
        this.pd = pd;
    }

    public PrePdBean getPrePd() {
        return prePd;
    }

    public void setPrePd(PrePdBean prePd) {
        this.prePd = prePd;
    }

    public static class PdBean {
        private int rownum;
        private String USER_IMAGE;
        private int TOTAL_VALUE;
        private String PHONE;
        private int USER_ID;
        private String USERNAME;

        public int getRownum() {
            return rownum;
        }

        public void setRownum(int rownum) {
            this.rownum = rownum;
        }

        public String getUSER_IMAGE() {
            return USER_IMAGE;
        }

        public void setUSER_IMAGE(String USER_IMAGE) {
            this.USER_IMAGE = USER_IMAGE;
        }

        public int getTOTAL_VALUE() {
            return TOTAL_VALUE;
        }

        public void setTOTAL_VALUE(int TOTAL_VALUE) {
            this.TOTAL_VALUE = TOTAL_VALUE;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }
    }

    public static class PrePdBean {
        private int rownum;
        private String USER_IMAGE;
        private int TOTAL_VALUE;
        private String PHONE;
        private int USER_ID;
        private String USERNAME;

        public int getRownum() {
            return rownum;
        }

        public void setRownum(int rownum) {
            this.rownum = rownum;
        }

        public String getUSER_IMAGE() {
            return USER_IMAGE;
        }

        public void setUSER_IMAGE(String USER_IMAGE) {
            this.USER_IMAGE = USER_IMAGE;
        }

        public int getTOTAL_VALUE() {
            return TOTAL_VALUE;
        }

        public void setTOTAL_VALUE(int TOTAL_VALUE) {
            this.TOTAL_VALUE = TOTAL_VALUE;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }
    }
}
