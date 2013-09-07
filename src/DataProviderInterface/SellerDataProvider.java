package DataProviderInterface;

import java.util.ArrayList;

import DataProvider.Deal;

public interface SellerDataProvider {
	public ArrayList<Deal> getSellerCurrentActiveDeals(String sellerId);

	public ArrayList<String> getSellerOfferedServices(String sellerId);

	public ArrayList<Deal> getSellerInactiveDeals(String sellerId);

	public void updateDealFromSeller(String sellerId, String dealId);

	public void cancelDealFromSeller(String sellerId, String dealId);

	public void addNewDealFromSeller(String sellerId);
}
