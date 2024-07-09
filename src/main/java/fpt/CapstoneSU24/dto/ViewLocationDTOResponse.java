package fpt.CapstoneSU24.dto;


public class ViewLocationDTOResponse {
   private int locationId;
   private String address;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
