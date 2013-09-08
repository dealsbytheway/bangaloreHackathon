package DataProviderInterface;

import java.sql.Timestamp;
import java.util.ArrayList;

import DataProvider.Deal;

public interface SellerDataProvider {
	public ArrayList<Deal> getSellerCurrentActiveDeals(String sellerId);

	public ArrayList<String> getSellerOfferedServices(String sellerId);

	public ArrayList<Deal> getSellerInactiveDeals(String sellerId);

	public void updateDealFromSeller(Deal deal);

	public void cancelDealFromSeller(String sellerId, String dealId);

	public void addNewDealFromSeller(String sellerId, ArrayList<String> tags,
			Timestamp startDate, Timestamp endDate, float discount);
}
