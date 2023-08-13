package com.apps.pochak.common;

public enum Status {
    PUBLIC {
        @Override
        public String toString() {
            return "PUBLIC";
        }
    }, PRIVATE {
        @Override
        public String toString() {
            return "PRIVATE";
        }
    }, DELETED {
        @Override
        public String toString() {
            return "DELETED";
        }
    }
}
