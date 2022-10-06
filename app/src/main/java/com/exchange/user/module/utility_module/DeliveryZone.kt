package com.exchange.user.module.utility_module

import com.exchange.user.module.restaurent_module.model.LatLongModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.DeliveryZones
import java.lang.Math.PI

object DeliveryZone {
    fun getDeliveryZone(point: LatLongModel, deliveryZones: ArrayList<DeliveryZones>?): Int { //, PointString : String
        if (deliveryZones != null) {
            for (index in 0 until deliveryZones.size) {
                val latlon = createLatLon(deliveryZones[index].pointString!!)
                if (this.contiansInPolygon(point,latlon)) {
                    return index
                }
            }
        }
        return -1
    }

   private fun contiansInPolygon(point : LatLongModel, bounds :  ArrayList<LatLongModel>): Boolean {
        var j = 0
        val oddNodes = false
        val x = point.log
        val y = point.lat
        for (i in 0 until bounds.size step 1){
            j++
            if (j == bounds.size){
                j = 0
            }
            bounds[i].lat = bounds[i].lat
            bounds[i].log = bounds[i].log
            bounds[j].lat = bounds[j].lat
            bounds[j].log = bounds[j].log
            if (bounds[i].lat < y && bounds[j].lat >= y || bounds[j].lat < y && bounds[i].lat >= y) {
                if (bounds[i].log + (y - bounds[i].lat) / (bounds[j].lat - bounds[i].lat) * (bounds[j].log - bounds[i].log) < x) {
                    return !oddNodes
                }
            }
        }
        return oddNodes
    }

    private fun createLatLon(pointString : String): ArrayList<LatLongModel> {
        val pointsarr  = pointString.split(",0,")
        val latlon = ArrayList<LatLongModel>()
        for (element in pointsarr){
            val points = element.split(",")
            val latlog = LatLongModel()
            latlog.lat = java.lang.Double.parseDouble(points[0])
            latlog.log = java.lang.Double.parseDouble(points[1])
            latlon.add(latlog)
        }
        return latlon
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = kotlin.math.sin(deg2rad(lat1)) * kotlin.math.sin(deg2rad(lat2)) + (kotlin.math.cos(
            deg2rad(lat1)
        )
                * kotlin.math.cos(deg2rad(lat2))
                * kotlin.math.cos(deg2rad(theta)))
        dist = kotlin.math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / PI
    }
}