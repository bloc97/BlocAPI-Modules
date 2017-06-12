/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.addon.RandomFact;

/**
 *
 * @author bowen
 */
public class HttpHelper {
    public static String getFromUrl(String url) {
        Scanner sc;
        String out = "";
        try {
            sc = new Scanner(new URL(url).openStream(), "UTF-8");
            out = sc.useDelimiter("\\A").next();
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(RandomFact.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
    public static String retrieveFromUrl(String url, String startSearch, String endSearch) {
        String out = getFromUrl(url);
        int beginIndex = out.indexOf(startSearch);
        int endIndex = out.indexOf(endSearch, beginIndex);
        if (out.length() < 1) {
            return "";
        }
        if (beginIndex < 0) {
            beginIndex = 1;
        }
        if (endIndex < beginIndex) {
            endIndex = beginIndex;
        }
        out = out.substring(beginIndex + startSearch.length(), endIndex);
        return out;
    }
}
