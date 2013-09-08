package DataProviderInterface;

import java.sql.Timestamp;
import java.util.ArrayList;

import DataProvider.Deal;

public interface SellerDataProvider {
	public ArrayList<Deal> getSellerCurrentActiveDeals(String sellerId);

	public ArrayList<String> getSellerOfferedServices(String sellerId);

	public ArrayList<Deal> getSellerInactiveDeals(String sellerId);

	public void updateDealFromSeller(Deal d);

	public void cancelDealFromSeller(String dealId);

	public void addNewDealFromSeller(String sellerId, ArrayList<String> tags,
			Timestamp startDate, Timestamp endDate, String discount);
}
