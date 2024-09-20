package Model;

import java.io.Serializable;

public class Brand implements Serializable {
    private String brandName;
    private String brandId;

    public Brand(String brandName, String brandId) {
        this.brandName = brandName;
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
