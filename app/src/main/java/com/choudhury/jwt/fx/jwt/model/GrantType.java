package com.choudhury.jwt.fx.jwt.model;

public enum GrantType {

    IMPLICIT {
        @Override
        public String toString() {
            return "Implicit";
        }
    },
    PCKE {
        @Override
        public String toString() {
            return "PCKE";
        }
    }
}
