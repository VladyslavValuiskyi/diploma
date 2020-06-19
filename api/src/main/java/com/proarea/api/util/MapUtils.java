package com.proarea.api.util;

import com.proarea.api.exception.BadRequestException;
import com.proarea.api.model.LatLng;
import org.springframework.stereotype.Service;

public class MapUtils {

    private static final Double DEFAULT_AREAL_METERS = 50.0;

    private static final Double METERS_IN_GRAD_LNG = 111321.377778;

    private static Double getMetersInDragLat(Double geoLat) {

        if (geoLat > 90 || geoLat < -90) {
            throw new BadRequestException("Incorrect latitude");
        }

        return 111321.377778 * Math.cos(geoLat);

    }

    private static Double getDistance(LatLng firstPos, LatLng secondPos) {

        Double latMiddle = (secondPos.getLatitude() + firstPos.getLatitude()) / 2;

        double latDiff = secondPos.getLatitude() - firstPos.getLatitude();
        double lngDiff = secondPos.getLongitude() - firstPos.getLongitude();

        double latDistance = Math.abs(latDiff) * getMetersInDragLat(latMiddle);
        double lngDistance = Math.abs(lngDiff) * METERS_IN_GRAD_LNG;

        return Math.sqrt(Math.pow(latDistance, 2) + Math.pow(lngDistance, 2));
    }

    public static boolean isInAreal(LatLng firstPos, LatLng secondPos) {
        if (firstPos == null || secondPos == null) {
            return false;
        }

        return getDistance(firstPos, secondPos) < DEFAULT_AREAL_METERS;

    }

    public static LatLng getLatLng(String lat, String lng) {
        try {
            Double latitude = Double.parseDouble(lat);
            Double longitude = Double.parseDouble(lng);

            return new LatLng(latitude, longitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
