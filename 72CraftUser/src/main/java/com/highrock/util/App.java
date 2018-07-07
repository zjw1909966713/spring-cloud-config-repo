package com.highrock.util;

public class App {
    public static void main(String[] args) {
       String aa= BCrypt.hashpw("123456", BCrypt.gensalt(8));

        String a=  BCrypt.hashpw("123456","$2a$08$b0MHMsT3ErLoTRjpjzsCie");

        boolean fa=BCrypt.checkpw("12345688888888888","$2a$10$ZN.qtjSE72wnOBbfi9B...kvrnkJuOMmk0qmDUas9sqvOoXrmZsuS");
        System.out.println(fa);
       // BCrypt.

        System.out.println(aa);
        System.out.println(a);
    }
}
