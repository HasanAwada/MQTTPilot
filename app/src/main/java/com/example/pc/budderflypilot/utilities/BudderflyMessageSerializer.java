package com.example.pc.budderflypilot.utilities;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Pc on 2/9/2018.
 */

public class BudderflyMessageSerializer {

    private static Gson gson = new Gson();

    public static byte[] toZippedByteArray(HashMap<String, Object> map) {
        try (
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                GZIPOutputStream zos = new GZIPOutputStream(byteOut);
                ObjectOutputStream out = new ObjectOutputStream(zos)
        ) {
            out.writeObject(map);
            out.flush();
            zos.finish();
            return byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HashMap<String, Object> fromZippedByteArray(byte[] bytes) {
        // Parse byte array to Map
        try (
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                GZIPInputStream zis = new GZIPInputStream(byteIn);
                ObjectInputStream in = new ObjectInputStream(zis)
        ) {
            return (HashMap<String, Object>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] toByteArray(HashMap<String, Object> map) {
        try (
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
        ) {
            out.writeObject(map);
            return byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HashMap<String, Object> fromByteArray(byte[] bytes) {
        // Parse byte array to Map
        try (
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(byteIn);
        ) {
            return (HashMap<String, Object>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
