package org.lld.ridesharingapplication.domain;

public class Vehicle {

    private String vehicleOwnerName;

    private String vehicleName;

    private VehicleType vehicleType;

    private String regNo;

    public Vehicle(String regNo,String vehicleName, String vehicleOwnerName) {
        this.regNo = regNo;
        this.vehicleOwnerName = vehicleOwnerName;
        this.vehicleName = vehicleName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleOwnerName() {
        return vehicleOwnerName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getRegNo() {
        return regNo;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleOwnerName='" + vehicleOwnerName + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", vehicleType=" + vehicleType +
                ", regNo='" + regNo + '\'' +
                '}';
    }
}
