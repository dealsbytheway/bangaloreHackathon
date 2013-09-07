package DataProviderInterface;

public interface SellerDataProvider {
    public void getSellerCurrentActiveDeals(String sellerId);
    public void getSellerOfferedServices(String sellerId);
    public void getSellerInactiveDeals(String sellerId);
    
    public void updateDealFromSeller(String sellerId, String dealId);
    public void cancelDealFromSeller(String sellerId, String dealId);
    public void addNewDealFromSeller(String sellerId);
}
