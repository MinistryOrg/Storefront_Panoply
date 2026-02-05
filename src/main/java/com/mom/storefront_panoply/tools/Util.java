package com.mom.storefront_panoply.tools;


import java.util.Collection;
import java.util.Map;

public class Util {
    public static boolean nullOrEmpty(Object obj) {
        boolean nullOrEmpty = obj == null ||
                (obj instanceof CharSequence && ((CharSequence) obj).isEmpty());

        if(obj instanceof Collection && ((Collection<?>) obj).isEmpty()) nullOrEmpty = true;
        if(obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) nullOrEmpty = true;

        return nullOrEmpty;
    }
}
