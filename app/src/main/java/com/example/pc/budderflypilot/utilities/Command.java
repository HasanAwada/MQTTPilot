package com.example.pc.budderflypilot.utilities;

/**
 * Created by Pc on 2/12/2018.
 */

public class Command {

    public static byte[] getTurnOnData(){
        byte [] data = new byte [1];
        data[0] = 33;
        return data;
    }

    public static byte[] getTurnOffData(){
        byte [] data = new byte [1];
        data[0] = 32;
        return data;
    }

}
