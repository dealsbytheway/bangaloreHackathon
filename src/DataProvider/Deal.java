package DataProvider;

import java.sql.Timestamp;

public class Deal {

	private String discountPercent;
	private String tag;
	private int dealId;
	private Timestamp startDate;
	private Timestamp endDate;
	private float latitude;
	private float longitude;

	public String getDiscountPercent() {
		return discountPercent;
	}

	public String getTag() {
		return tag;
	}

	public int getDealId() {
		return dealId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

}
