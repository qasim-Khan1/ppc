package tin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.threeten.bp.LocalDateTime;

import javax.annotation.Nullable;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v8.common.AdTextAsset;
import com.google.ads.googleads.v8.common.ExpandedTextAdInfo;
import com.google.ads.googleads.v8.common.KeywordInfo;
import com.google.ads.googleads.v8.common.ResponsiveSearchAdInfo;
import com.google.ads.googleads.v8.enums.AdGroupAdRotationModeEnum.AdGroupAdRotationMode;
import com.google.ads.googleads.v8.enums.AdGroupAdStatusEnum.AdGroupAdStatus;
import com.google.ads.googleads.v8.enums.AdGroupCriterionStatusEnum.AdGroupCriterionStatus;
import com.google.ads.googleads.v8.enums.AdGroupStatusEnum.AdGroupStatus;
import com.google.ads.googleads.v8.enums.CriterionTypeEnum.CriterionType;
import com.google.ads.googleads.v8.enums.KeywordMatchTypeEnum.KeywordMatchType;
import com.google.ads.googleads.v8.resources.Ad;
import com.google.ads.googleads.v8.resources.AdGroup;
import com.google.ads.googleads.v8.resources.AdGroupAd;
import com.google.ads.googleads.v8.resources.AdGroupCriterion;
import com.google.ads.googleads.v8.resources.AdParameter;
import com.google.ads.googleads.v8.resources.CampaignCriterion;
import com.google.ads.googleads.v8.resources.Customer;
import com.google.ads.googleads.v8.services.AdGroupAdOperation;
import com.google.ads.googleads.v8.services.AdGroupAdServiceClient;
import com.google.ads.googleads.v8.services.AdGroupCriterionOperation;
import com.google.ads.googleads.v8.services.AdGroupCriterionServiceClient;
import com.google.ads.googleads.v8.services.AdGroupOperation;
import com.google.ads.googleads.v8.services.AdGroupServiceClient;
import com.google.ads.googleads.v8.services.AdParameterOperation;
import com.google.ads.googleads.v8.services.AdParameterServiceClient;
import com.google.ads.googleads.v8.services.CampaignCriterionOperation;
import com.google.ads.googleads.v8.services.CampaignCriterionServiceClient;
import com.google.ads.googleads.v8.services.CreateCustomerClientResponse;
import com.google.ads.googleads.v8.services.CustomerServiceClient;
import com.google.ads.googleads.v8.services.GoogleAdsRow;
import com.google.ads.googleads.v8.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v8.services.MutateAdGroupAdResult;
import com.google.ads.googleads.v8.services.MutateAdGroupAdsResponse;
import com.google.ads.googleads.v8.services.MutateAdGroupCriteriaResponse;
import com.google.ads.googleads.v8.services.MutateAdGroupCriterionResult;
import com.google.ads.googleads.v8.services.MutateAdGroupResult;
import com.google.ads.googleads.v8.services.MutateAdGroupsResponse;
import com.google.ads.googleads.v8.services.MutateCampaignCriteriaRequest;
import com.google.ads.googleads.v8.services.MutateCampaignCriteriaResponse;
import com.google.ads.googleads.v8.services.MutateCampaignCriterionResult;
import com.google.ads.googleads.v8.services.MutateOperation;
import com.google.ads.googleads.v8.services.SearchGoogleAdsRequest;
import com.google.ads.googleads.v8.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v8.services.SearchGoogleAdsStreamResponse;
import com.google.ads.googleads.v8.services.GoogleAdsServiceClient.SearchPagedResponse;
import com.google.ads.googleads.v8.utils.ResourceNames;
import com.google.api.gax.rpc.ServerStream;
import com.google.auth.oauth2.UserCredentials;
import com.google.ads.googleads.v8.services.CampaignBidModifierServiceClient;
import com.google.ads.googleads.v8.services.MutateCampaignBidModifiersResponse;
import com.google.ads.googleads.v8.services.CampaignBudgetServiceClient;
import com.google.ads.googleads.v8.services.CampaignExtensionSettingOperation;
import com.google.ads.googleads.v8.services.CampaignExtensionSettingServiceClient;
import com.google.ads.googleads.v8.services.CampaignOperation;
import com.google.ads.googleads.v8.services.CampaignServiceClient;
import com.google.ads.googleads.v8.services.CustomerOperation;
import com.google.ads.googleads.v8.services.ExtensionFeedItemOperation;
import com.google.ads.googleads.v8.services.ExtensionFeedItemServiceClient;
import com.google.ads.googleads.v8.services.GoogleAdsServiceClient.SearchPage;
import com.google.ads.googleads.v8.services.LabelOperation;
import com.google.ads.googleads.v8.services.LabelServiceClient;
import com.google.ads.googleads.v8.services.MutateAdGroupCriterionLabelResult;
import com.google.ads.googleads.v8.services.MutateAdGroupCriterionLabelsRequest;
import com.google.ads.googleads.v8.services.MutateAdGroupCriterionLabelsResponse;
import com.google.ads.googleads.v8.common.CalloutFeedItem;
import com.google.ads.googleads.v8.common.StructuredSnippetFeedItem; //added new
import com.google.ads.googleads.v8.common.InteractionTypeInfo;
import com.google.ads.googleads.v8.common.LanguageInfo;
import com.google.ads.googleads.v8.common.LocationInfo;
import com.google.ads.googleads.v8.common.ManualCpc;
import com.google.ads.googleads.v8.common.SitelinkFeedItem;
import com.google.ads.googleads.v8.common.TargetCpa;
import com.google.ads.googleads.v8.common.TargetSpend;
import com.google.ads.googleads.v8.enums.AdvertisingChannelTypeEnum.AdvertisingChannelType;
import com.google.ads.googleads.v8.enums.BiddingStrategyStatusEnum.BiddingStrategyStatus;
import com.google.ads.googleads.v8.enums.BiddingStrategyTypeEnum.BiddingStrategyType;
import com.google.ads.googleads.v8.enums.BudgetDeliveryMethodEnum.BudgetDeliveryMethod;
import com.google.ads.googleads.v8.enums.BudgetStatusEnum.BudgetStatus;
import com.google.ads.googleads.v8.enums.CampaignStatusEnum.CampaignStatus;
import com.google.ads.googleads.v8.enums.ExtensionTypeEnum.ExtensionType;
import com.google.ads.googleads.v8.enums.FeedItemStatusEnum.FeedItemStatus;
import com.google.ads.googleads.v8.enums.FeedItemTargetDeviceEnum.FeedItemTargetDevice;
import com.google.ads.googleads.v8.enums.InteractionTypeEnum.InteractionType;
import com.google.ads.googleads.v8.enums.NegativeGeoTargetTypeEnum.NegativeGeoTargetType;
import com.google.ads.googleads.v8.enums.PositiveGeoTargetTypeEnum.PositiveGeoTargetType;
import com.google.ads.googleads.v8.enums.ResponseContentTypeEnum.ResponseContentType;
import com.google.ads.googleads.v8.resources.AdGroupCriterionLabel;
import com.google.ads.googleads.v8.resources.BiddingStrategy;
import com.google.ads.googleads.v8.resources.Campaign;
import com.google.ads.googleads.v8.resources.Campaign.GeoTargetTypeSetting;
import com.google.ads.googleads.v8.resources.Campaign.NetworkSettings;
import com.google.ads.googleads.v8.resources.CampaignBidModifier;
import com.google.ads.googleads.v8.resources.CampaignBudget;
import com.google.ads.googleads.v8.resources.CampaignExtensionSetting;
import com.google.ads.googleads.v8.resources.ExtensionFeedItem;
import com.google.ads.googleads.v8.resources.Label;
import com.google.ads.googleads.v8.services.AdGroupCriterionLabelOperation;
import com.google.ads.googleads.v8.services.AdGroupCriterionLabelServiceClient;
import com.google.ads.googleads.v8.services.BiddingStrategyOperation;
import com.google.ads.googleads.v8.services.BiddingStrategyServiceClient;
import com.google.ads.googleads.v8.services.CampaignBidModifierOperation;
import com.google.ads.googleads.v8.services.CampaignBudgetOperation;
import com.google.ads.googleads.v8.services.MutateBiddingStrategiesResponse;
import com.google.ads.googleads.v8.services.MutateCampaignBidModifiersRequest;
import com.google.ads.googleads.v8.services.MutateCampaignBudgetsResponse;
import com.google.ads.googleads.v8.services.MutateCampaignExtensionSettingResult;
import com.google.ads.googleads.v8.services.MutateCampaignExtensionSettingsResponse;
import com.google.ads.googleads.v8.services.MutateCampaignResult;
import com.google.ads.googleads.v8.services.MutateCampaignsResponse;
import com.google.ads.googleads.v8.services.MutateCustomerResponse;
import com.google.ads.googleads.v8.services.MutateExtensionFeedItemResult;
import com.google.ads.googleads.v8.services.MutateExtensionFeedItemsResponse;
import com.google.ads.googleads.v8.services.MutateLabelResult;
import com.google.ads.googleads.v8.services.MutateLabelsResponse;
import com.google.common.collect.ImmutableList;


public class Master {
	private static final int PAGE_SIZE = 1_000;
	
	public static GoogleAdsClient setCredentials(String refreshToken, String clientId, String clientSecret, Long clientCustomerId, String developerToken) throws Throwable {
		/**
		 * This function returns a session variable after creating a googleAdsClient session using user credentials
		 * 
		 * @param refreshToken the refresh token
		 * @param clientId the client id of google ads account
		 * @param clientSecret the client secret key
		 * @param clientCustomerId the client customer id
		 * @param developerToken the developer token
		 * 
		 * @returns a GoogleAdsClient session
		 */		
		
		// Set user credentials
		UserCredentials credentials = UserCredentials.newBuilder()
				 .setClientId(clientId)
				 .setClientSecret(clientSecret)
				 .setRefreshToken(refreshToken)
				 .build();
		
		// return a googleAdsClient session after creating a session using the user credentials
		return GoogleAdsClient.newBuilder()
				 .setLoginCustomerId(clientCustomerId)
				 .setCredentials(credentials)
				 .setDeveloperToken(developerToken)
				 .build();
	}
		
	public static void GetCampaigns(GoogleAdsClient googleAdsClient, long customerId) throws Throwable {
		 GoogleAdsServiceClient googleAdsServiceClient =
		        googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		  String query = "SELECT campaign.id, campaign.name FROM campaign ORDER BY campaign.id";
		  // Constructs the SearchGoogleAdsStreamRequest.
		  SearchGoogleAdsStreamRequest request =
		      SearchGoogleAdsStreamRequest.newBuilder()
								          .setCustomerId(Long.toString(customerId))
								          .setQuery(query)
								          .build();
		  // Creates and issues a search Google Ads stream request that will retrieve all campaigns.
		  ServerStream<SearchGoogleAdsStreamResponse> stream = 
				  googleAdsServiceClient.searchStreamCallable().call(request);
		
		  // Iterates through and prints all of the results in the stream response.
		  for (SearchGoogleAdsStreamResponse response : stream) {
		    for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
		      System.out.printf(
		          "Campaign with ID %d and name '%s' was found.%n",
		      googleAdsRow.getCampaign().getId(), googleAdsRow.getCampaign().getName());
		    }
		  }
	}
	public static String getadgroup(GoogleAdsClient googleAdsClient, long customerId,  @Nullable Long campaignId) throws Throwable {
		/**
		 * This function returns the list of ad groups against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the ad groups
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of ad groups against a campaign Id.
		 */
		 GoogleAdsServiceClient googleAdsServiceClient =
			        googleAdsClient.getLatestVersion().createGoogleAdsServiceClient(); 
	      String searchQuery = "SELECT campaign.id, ad_group.id, ad_group.name, ad_group.ad_rotation_mode,"
	      		+ " ad_group.status, ad_group.cpc_bid_micros, ad_group.target_cpa_micros, ad_group.labels "
	      		+ " FROM ad_group where ad_group.status in ('ENABLED','PAUSED','REMOVED')";
	      if (campaignId != null) {
	        searchQuery += String.format(" and campaign.id = %d", campaignId);
	      }
	      SearchGoogleAdsRequest request =
	          SearchGoogleAdsRequest.newBuilder()
	              .setCustomerId(Long.toString(customerId))
	              .setPageSize(PAGE_SIZE)
	              .setQuery(searchQuery)
	              .build();
	      // Issues the search request.
	      SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
	      // Iterates over all rows in all pages and prints the requested field values for the ad group
	      // in each row.
	      for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	        AdGroup adGroup = googleAdsRow.getAdGroup();
	        System.out.printf(
	            "Ad group with ID %d and name '%s' was found in campaign with ID %d.%n",
	            adGroup.getId(), adGroup.getName(), googleAdsRow.getCampaign().getId());
	      }
	      
	      String out = "";
		for(GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {	
			AdGroup item = googleAdsRow.getAdGroup();
			out = out + campaignId.toString() + ";" 
					+ item.getId() + ";" + item.getName().toString() + ";"
					+ item.getStatus().toString() + ";"
					+ item.getCpcBidMicros() + ";"
					+ '~';
		}
		return out;
	}
	
	public static String addAdGroup(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable{
		/**
		 * This function adds new ad groups using the data provided
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "campaign_id;adw_agroup_name;status;max_cpc;rotation;table_id~
		 * 									campaign_id;adw_agroup_name;status;max_cpc;rotation;table_id~
		 * 									..."
		 * 
		 * @returns a string "table_id; adgroup_id~
		 * 					  table_id; adgroup_id~
		 * 					  ..."
		 */
		
		// Create a list of adgroups by splitting the data string on ~
	    List<String> list_of_groups = new ArrayList<>();
	    list_of_groups.addAll(Arrays.asList(data.split("~")));
	    
	    String output = "";
	    long[] table_ids = new long[list_of_groups.size()];
	    
	    // Create a list of operations
	    List<AdGroupOperation> operations = new ArrayList<>();
	    
	    
	    // Iterate through the list of adgroups created above and split data further on ;
	    for(int i = 0; i < list_of_groups.size(); i++) {
	    	String[] values = list_of_groups.get(i).split(";");
	    	String campaignResourceName = ResourceNames.campaign(customerId, Long.parseLong(values[0]));
	    	String adw_adgroup_name = values[1];
	    	String status = values[2];
	    	float max_cpc1 = Float.parseFloat(values[3]);
	    	long max_cpc =  (long) (max_cpc1 * 1000000);
	    	String rotation = values[4];
	    	String table_id = values[5];
	    	
	    	// Add table_id in a list index-wise to use it later in the data output string.
	    	table_ids[i] = Long.parseLong(table_id);
	    	AdGroup adGroup = AdGroup.newBuilder()
    								 .setName(adw_adgroup_name)
    								 .setStatus(AdGroupStatus.valueOf(status))
	    							 .setCampaign(campaignResourceName)
	    							 .setCpcBidMicros(max_cpc)
	    							 .setAdRotationMode(AdGroupAdRotationMode.valueOf(rotation))
	    							 .build();
	    	
	    	// Create a new operation and add it in the list of operations.
	    	AdGroupOperation op = AdGroupOperation.newBuilder().setCreate(adGroup).build();
			operations.add(op);
			
	    }
	    // Mutate the list of operations created above
	    AdGroupServiceClient adGroupServiceClient = googleAdsClient.getLatestVersion().createAdGroupServiceClient(); 
		MutateAdGroupsResponse adGroup_response = adGroupServiceClient.mutateAdGroups(String.valueOf(customerId), operations);
		
		// Iterate through the list of results and generate an output string conatins table_ids and newly created adgroup ids
		int index = 0;
	    for (MutateAdGroupResult result : adGroup_response.getResultsList()) {
	    	output += table_ids[index];
	    	output += ";";
	    	output += result.getResourceName().split("/")[3];
	        output += "~";
	        index+=1;
	    }
	    
	    // Removes the ~ on last index
	    output = output.substring(0,output.length() - 1);
	    return output;
	}

	public static String addNegativeKeyword(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	{
		List<CampaignCriterionOperation> operations = new ArrayList<>();
		String[] aaa = Data.split("~");
		String[] keyword_table_id = new String[aaa.length];
		for(int i = 0 ; i < aaa.length ; i++)
		{
			String[] bbb = aaa[i].split(";");
			
			String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(bbb[0]));
			
			KeywordInfo li = KeywordInfo.newBuilder().setText(bbb[1]).setMatchType(KeywordMatchType.valueOf(bbb[2])).build();
			keyword_table_id[i] = bbb[3];
			CampaignCriterion builder = MutateOperation.newBuilder()
					.getCampaignCriterionOperationBuilder().getCreateBuilder()
					.setCampaign(campaignResourceName)
					.setType(CriterionType.KEYWORD)
					.setNegative(true)
					.setKeyword(li)
					.build();
	        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
	        operations.add(o);
		}
		MutateCampaignCriteriaRequest Mutate_Camp_asset_request = MutateCampaignCriteriaRequest
																	.newBuilder()
																	.addAllOperations(operations)
																	.setCustomerId(String.valueOf(customerId)).build();
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(Mutate_Camp_asset_request);
        int i = 0;
        String response = ""; 
        for (MutateCampaignCriterionResult result : response_.getResultsList())
        {
        	response = response + keyword_table_id[i] + ";";
        	String[] v = result.getResourceName().split("/");
        	response = response + v[5] + "~";
        	i++;
        }
        response = response.substring(0,response.length() - 1);
        response = response.replace("", "");   
        return response;
	}
	public static String addExpandedTextAds(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable {
		/**
		 * This function adds new Expended Text Ad using the data provided
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adgroupId;headline1;headline2;headline3;description1;description2;path1;path2;finalUrl;status;eta_id~
		 * 									adgroupId;headline1;headline2;headline3;description1;description2;path1;path2;finalUrl;status;eta_id~
		 * 									..."
		 * 
		 * @returns a string "eta_id; adw_eta_id~
		 * 					  eta_id; adw_eta_id~
		 * 					  ..."
		*/
		
		// Create a list of ads and insert ~ separated records in the list
		List<String> list_of_ads = new ArrayList<>();
	    list_of_ads.addAll(Arrays.asList(data.split("~")));
	    String output = "";
	    long[] etaIds = new long[list_of_ads.size()];
	    
	    // Create a list of operations
	    List<AdGroupAdOperation> operations = new ArrayList<>();

	    // Iterate through the list of ETAds created above and split data further on ;
	    for(int i = 0; i < list_of_ads.size(); i++) {
	    	String[] values = list_of_ads.get(i).split(";");
	    	long adGroupId = Long.parseLong(values[0]);
	    	String headline1 = values[1];
	    	String headline2 = values[2];
	    	String headline3 = values[3];
	    	String description1 = values[4];
	    	String description2 = values[5];
	    	String path1 = values[6];
	    	String path2 = values[7];
	    	String finalUrl = values[8];
	    	String status = values[9];
	    	String eta_id = values[10];
	    	etaIds[i] = Long.parseLong(eta_id);
	    	String adGroupResourceName = ResourceNames.adGroup(customerId, adGroupId);
	    	ExpandedTextAdInfo expandedTextAdInfo = 
	    			ExpandedTextAdInfo.newBuilder()
	    				.setHeadlinePart1(headline1)
	    				.setHeadlinePart2(headline2)
	    				.setHeadlinePart3(headline3)
	    				.setDescription(description1)
	    				.setDescription2(description2)
	    				.setPath1(path1)
	    				.setPath2(path2)
	    				.build();
	    	Ad ad = Ad.newBuilder()
	    				.setExpandedTextAd(expandedTextAdInfo)
	    				.addFinalUrls(finalUrl)
	    				.build();
	    	AdGroupAd adGroupAd = 
	    			AdGroupAd.newBuilder()
	    				.setAdGroup(adGroupResourceName)
	    				.setAd(ad)
	    				.setStatus(AdGroupAdStatus.valueOf(status))
	    				.build();
	    	AdGroupAdOperation op = AdGroupAdOperation.newBuilder()
	    								.setCreate(adGroupAd)
	    								.build();
	    	operations.add(op);
		}
	    AdGroupAdServiceClient adGroupAdServiceClient = googleAdsClient.getLatestVersion().createAdGroupAdServiceClient();
	    MutateAdGroupAdsResponse response =  adGroupAdServiceClient.mutateAdGroupAds(Long.toString(customerId), operations);
	    int index = 0;
	    for (MutateAdGroupAdResult result : response.getResultsList()) {
	    	System.out.printf(
	    			"Expanded text ad created with resource name: %s%n", result.getResourceName());
	
			output += etaIds[index];
			output += ";";
			output += result.getResourceName().split("~")[1];
			output += "~";
			index+=1;
	    }
	    output = output.substring(0,output.length() - 1);
	    return output;
	}
	private static AdTextAsset createAdTextAsset(String text) throws Throwable {
	    return AdTextAsset.newBuilder().setText(text).build();
	}
	public static String addResponsiveSearchAds(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable {
		/**
		 * This function adds new Responsive Search Ads using the data provided
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adgroupId;headline1;headline2;...;headline15;description1;description2;path1;path2;finalUrl;status;rsa_id~
		 * 									adgroupId;headline1;headline2;...;headline15;description1;description2;path1;path2;finalUrl;status;rsa_id~
		 * 									..."
		 * 
		 * @returns a string "rsa_id; adw_rsa_id~
		 * 					  rsa_id; adw_rsa_id~
		 * 					  ..."
		*/
		
		List<String> list_of_ads = new ArrayList<>();
	    list_of_ads.addAll(Arrays.asList(data.split("~")));
	    String output = "";
	    long[] rsaIds = new long[list_of_ads.size()];
	    
	    // A list to store Operations
	    List<AdGroupAdOperation> operations = new ArrayList<>();
	    
	    // A list to store headlines
		List<AdTextAsset> headlines = new ArrayList<>();

	    // Iterate through the list of ETAds created above and split data further on ;
	    for(int i = 0; i < list_of_ads.size(); i++) {
	    	String[] values = list_of_ads.get(i).split(";");
	    	
	    	long adGroupId = Long.parseLong(values[0]);
	    	System.out.println(values.length);
	    	
	    	// Use the @createAdTextAsset function to create an AdTextAsset and add it in the headlines list
	    	for (int h = 1; h<=15; h++) {
	    		headlines.add(createAdTextAsset(values[h]));
	    	}
	    	
	    	String description1 = values[16];
	    	String description2 = values[17];
	    	String description3 = values[18];
	    	String description4 = values[19];
	    	String path1 = values[20];
	    	String path2 = values[21];
	    	String finalUrl = values[22];
	    	String status = values[23];
	    	String rsa_id = values[24];
	    	
	    	rsaIds[i] = Long.parseLong(rsa_id);
	    	String adGroupResourceName = ResourceNames.adGroup(customerId, adGroupId);
	    	
	    	ResponsiveSearchAdInfo responsiveSearchAdInfo = ResponsiveSearchAdInfo.newBuilder()
	    													// Using addAllHeadlines function to insert all the headlines in the list
	    														.addAllHeadlines(headlines)
	    														.addDescriptions(createAdTextAsset(description1))
	    														.addDescriptions(createAdTextAsset(description2))
	    														.addDescriptions(createAdTextAsset(description3))
	    														.addDescriptions(createAdTextAsset(description4))
	    														.setPath1(path1)
	    														.setPath2(path2)
	    														.build();
	    	// Creates an Ad
	    	Ad ad = Ad.newBuilder()
    				.setResponsiveSearchAd(responsiveSearchAdInfo)
    				.addFinalUrls(finalUrl)
    				.build();
	    	
	    	// Set ad group in using the adGroupId
	    	AdGroupAd adGroupAd =
	    		        AdGroupAd.newBuilder()
	    		            .setAdGroup(adGroupResourceName)
	    		            .setStatus(AdGroupAdStatus.valueOf(status))
	    		            .setAd(ad)
	    		            .build();
	    	
	    	// Create an operation and add it in the operation list
	    	AdGroupAdOperation operation = AdGroupAdOperation.newBuilder().setCreate(adGroupAd).build();
	    	operations.add(operation);
	    }
	    AdGroupAdServiceClient adGroupAdServiceClient = googleAdsClient.getLatestVersion().createAdGroupAdServiceClient();
	    MutateAdGroupAdsResponse response = adGroupAdServiceClient.mutateAdGroupAds(Long.toString(customerId), operations);
	    int index = 0;
	    
	    // Generates the output string using the value of result
	    for (MutateAdGroupAdResult result : response.getResultsList()) {
//	    	System.out.printf(
//	    			"Expanded text ad created with resource name: %s%n", result.getResourceName());
	
			output += rsaIds[index];
			output += ";";
			output += result.getResourceName().split("~")[1];
			output += "~";
				index+=1;
			System.out.println(result.getResourceName());
	    }
	    
	    // Removes the ~ on the last index
	    output = output.substring(0,output.length() - 1);
	    return output;
	}
	public static String addKeywords(GoogleAdsClient googleAdsClient, long customerId,  String data) throws Throwable {
		/**
		 * This function adds new keywords in an adgroup
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adgroupId;keywordId;keywordText;finalUrl;matchType;status~
		 * 									adgroupId;keywordId;keywordText;finalUrl;matchType;status~
		 * 									..."
		 * 
		 * @returns a string "keyword_id; adw_keyword_id~
		 * 					  keyword_id; adw_keyword_id~
		 * 					  ..."
		*/
		
		// Creates a list of operation
		List<AdGroupCriterionOperation> operations = new ArrayList<>();
		
		// Creates a list of keyword records ~ separated
		List<String> list_of_keywords = new ArrayList<>();
	    list_of_keywords.addAll(Arrays.asList(data.split("~")));
	    String output = "";
	    long[] keyword_ids = new long[list_of_keywords.size()];
	    
	    // Iterate through the list of keywords and split each record on ; to get values
	    for(int i = 0; i < list_of_keywords.size(); i++) {
	    	String[] values = list_of_keywords.get(i).split(";");
	    	
	    	String adGroupId = values[0];
	    	String keywordId = values[1];
	    	String keywordText = values[2];
	    	String finalUrl = values[3];
	    	String matchType = values[4];
	    	String status = values[5];
	    	
	    	keyword_ids[i] = Long.parseLong(keywordId);
	    	
	    	KeywordInfo keywordInfo = KeywordInfo.newBuilder()
	    										 .setText(keywordText)
	    										 .setMatchType(KeywordMatchType.valueOf(matchType))
	    										 .build();
	    
	    	 String adGroupResourceName = ResourceNames.adGroup(customerId, Long.parseLong(adGroupId));
	    	 
	    	 AdGroupCriterion criterion =
	 		        AdGroupCriterion.newBuilder()
	 		            .setAdGroup(adGroupResourceName)
	 		            .setStatus(AdGroupCriterionStatus.valueOf(status))
	 		            .setKeyword(keywordInfo)
	 		            .addFinalUrls(finalUrl)
	 		            .build();
	    	 
	    	 // Creates an operation and insert in the operation list.
	    	 AdGroupCriterionOperation op =
	 		        AdGroupCriterionOperation.newBuilder().setCreate(criterion).build();
	    	 
	    	 operations.add(op);
	     }
	    AdGroupCriterionServiceClient agcServiceClient =
		        googleAdsClient.getLatestVersion().createAdGroupCriterionServiceClient();
	    
	    MutateAdGroupCriteriaResponse response =
		          agcServiceClient.mutateAdGroupCriteria(Long.toString(customerId), operations);
	    
	    // Iterate through the list of results and generate the output string
	    int index = 0;
        for (MutateAdGroupCriterionResult result : response.getResultsList()) {
		    output += keyword_ids[index];
			output += ";";
			output += result.getResourceName().split("~")[1];
			output += "~";
			index+=1;
			System.out.println(result.getResourceName());
        }
        
	    // Removes ~ at the last index of string.   
		output = output.substring(0,output.length() - 1);
	    return output;
	}
	public static void addParam1(GoogleAdsClient googleAdsClient, String customerId, String data) throws Throwable {
		/**
		 * This function adds param1
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adGroupId;keywordId;insertionText~
		 * 									adGroupId;keywordId;insertionText~
		 * 									..."
		 */
		
		// Creates a list of operation
		List<AdParameterOperation> operations = new ArrayList<>();
		
		// Creates a list of parameters and insert data string after splitting on ~
		List<String> list_of_params = new ArrayList<>();
		list_of_params.addAll(Arrays.asList(data.split("~")));
		
		// Iterate through the list of parameters and get values to process by splitting on ;
	    for(int i = 0; i < list_of_params.size(); i++) {
	    	String[] values = list_of_params.get(i).split(";");
	    	Long adGroupId = Long.parseLong(values[0]);
	    	Long keywordId = Long.parseLong(values[1]);
	    	String insertionText = values[2];
	    	String keywordResourceName = ResourceNames.keywordView(Long.parseLong(customerId), adGroupId, keywordId);
	    	AdParameter builder = AdParameter.newBuilder()
	    									 .setAdGroupCriterion(keywordResourceName)
	    									 
	    									 // Index 1 represents param1
	    									 .setParameterIndex(1)
	    									 .setInsertionText(insertionText)
	    									 .build();
	    	
	    	// Creates an operation and insert it in a list 
	    	AdParameterOperation op = AdParameterOperation.newBuilder().setCreate(builder).build();
	    	operations.add(op);
	    }
		AdParameterServiceClient addParamServiceClient = googleAdsClient.getLatestVersion().createAdParameterServiceClient();
        addParamServiceClient.mutateAdParameters(customerId,operations);
	}
	
	public static void addParam2(GoogleAdsClient googleAdsClient, String customerId, String data) throws Throwable {
		/**
		 * This function adds param2
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adGroupId;keywordId;insertionText~
		 * 									adGroupId;keywordId;insertionText~
		 * 									..."
		 */
		
		// Creates a list of operation
		List<AdParameterOperation> operations = new ArrayList<>();

		// Creates a list of parameters and insert data string after splitting on ~
		List<String> list_of_params = new ArrayList<>();
		list_of_params.addAll(Arrays.asList(data.split("~")));

		// Iterate through the list of parameters and get values to process by splitting on ;
	    for(int i = 0; i < list_of_params.size(); i++) {
	    	String[] values = list_of_params.get(i).split(";");
	    	
	    	Long adGroupId = Long.parseLong(values[0]);
	    	Long keywordId = Long.parseLong(values[1]);
	    	String insertionText = values[2];
	    	
	    	String keywordResourceName = ResourceNames.keywordView(Long.parseLong(customerId), adGroupId, keywordId);
	    	AdParameter builder = AdParameter.newBuilder()
	    									 .setAdGroupCriterion(keywordResourceName)

	    									 // Index 2 represents param2
	    									 .setParameterIndex(2)
	    									 .setInsertionText(insertionText)
	    									 .build();
	    	
	    	// Creates an operation and insert it in a list 
	    	AdParameterOperation op = AdParameterOperation.newBuilder().setCreate(builder).build();
	    	operations.add(op);
	    }
	    
	    // Mutates the list of operations
		AdParameterServiceClient addParamServiceClient = googleAdsClient.getLatestVersion().createAdParameterServiceClient();
        addParamServiceClient.mutateAdParameters(customerId,operations);
	}
	@SuppressWarnings("unused")
	public static String updateKeywordStatus(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable {
		/**
		 * This function updates Keyword Status
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adGroupId;keywordId;status;tableId~
		 * 									adGroupId;keywordId;status;tableId~
		 * 									..."
		 * 
		 * @returns a string containing ~ separated tableIds 
		 */
		
		// Creates a list of operations
		List<AdGroupCriterionOperation> operations = new ArrayList<>();
		
		// Creates a list of keyword status after splitting on ~
		List<String> list_of_keyword_status = new ArrayList<>();
		list_of_keyword_status.addAll(Arrays.asList(data.split("~")));
		String output = "";
		AdGroupCriterionServiceClient adGroupCriterionServiceClient = googleAdsClient.getLatestVersion()
				 .createAdGroupCriterionServiceClient();
		List<String> tableIds = new ArrayList<>(); 
		

		// Iterate through the list of parameters and get values to process by splitting on ;
	    for(int i = 0; i < list_of_keyword_status.size(); i++) {
	    	String[] values = list_of_keyword_status.get(i).split(";");
	    	
	    	Long adGroupId = Long.parseLong(values[0]);
	    	Long keywordId = Long.parseLong(values[1]);
	    	String status = values[2];
	    	String tableId = values[3];
	    	tableIds.add(tableId);
	    	
	    	// Creates adGroupCriterion 
	    	AdGroupCriterion adGroupCriterion = AdGroupCriterion.newBuilder()
				    .setResourceName(ResourceNames.adGroupCriterion(customerId, adGroupId, keywordId))
				    .setStatus(AdGroupCriterionStatus.valueOf(status))
					.build();
	    	
	    	// Creates an operation and add it in the list of operations
	    	AdGroupCriterionOperation operation = AdGroupCriterionOperation.newBuilder()
			        .setUpdate(adGroupCriterion)
			        .setUpdateMask(FieldMasks.allSetFieldsOf(adGroupCriterion))
			        .build();
	    	
	    	operations.add(operation);

	    }
	    // Mutate the operation list and store the response in a variable
		MutateAdGroupCriteriaResponse response = adGroupCriterionServiceClient.mutateAdGroupCriteria(
		        Long.toString(customerId), operations);
		// Iterate through the results list and generate an output string
		int index = 0;
		for (MutateAdGroupCriterionResult mutateAdGroupCriterionResult : response.getResultsList()) {
				output += tableIds.get(index);
				output += "~";
				index++;
		}
		output = output.substring(0,output.length() - 1);
		return output;
	}

	public static String geteta(GoogleAdsClient googleAdsClient, long customerId, @Nullable Long adGroupId) throws Throwable {
		/**
		 * This function returns the list of ETAs in an adGroup
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param adGroupId the adGroupId 
		 * 
		 * @returns a string "adgroup_id;ad_id;status;headline1;headline2;headline3;description1;description2;path1;path2;finalUrl~
		 * 					  adgroup_id;ad_id;status;headline1;headline2;headline3;description1;description2;path1;path2;finalUrl~
		 * 					  ..."
		 */
	    GoogleAdsServiceClient googleAdsServiceClient =
	            googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
	    
	    // Query to fetch the required fields
	    String searchQuery = "SELECT ad_group_ad.ad.expanded_text_ad.headline_part1,"
	    		+ " ad_group_ad.ad.expanded_text_ad.headline_part2,"
	    		+ " ad_group_ad.ad.expanded_text_ad.headline_part3,"
	    		+ " ad_group_ad.ad.expanded_text_ad.description,"
	    		+ " ad_group_ad.ad.expanded_text_ad.description2,"
	    		+ " ad_group_ad.ad.final_urls,"
	    		+ " ad_group_ad.status,"
	    		+ " ad_group_ad.ad.call_ad.path1,"
	    		+ " ad_group_ad.ad.call_ad.path2,"
	    		+ " ad_group_ad.ad.id"
	    		+ " FROM ad_group_ad"
	    		+ " WHERE ad_group_ad.ad.type = 'EXPANDED_TEXT_AD' ";
          if (adGroupId != null) {
            searchQuery += String.format("AND ad_group.id = %d", adGroupId);
          }
          // Creates a request that will retrieve all ads using pages of the specified page size.
          SearchGoogleAdsRequest request =
              SearchGoogleAdsRequest.newBuilder()
                  .setCustomerId(Long.toString(customerId))
                  .setPageSize(PAGE_SIZE)
                  .setQuery(searchQuery)
                  .build();
          // Issues the search request.
          SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
          // Iterates over all rows in all pages and prints the requested field values for the ad
          // in each row.
          String output = "";
          
          // Iterate through the result and create an output string containing the ~ separated records with ; separated values
          for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
            Ad ad = googleAdsRow.getAdGroupAd().getAd();
            ExpandedTextAdInfo expandedTextAdInfo = ad.getExpandedTextAd();
            output = output + adGroupId.toString() 
            				+ ";" + ad.getId()
            				+ ";" + googleAdsRow.getAdGroupAd().getStatus()
            				+ ";" + expandedTextAdInfo.getHeadlinePart1()
        					+ ";" + expandedTextAdInfo.getHeadlinePart2()
    						+ ";" + expandedTextAdInfo.getHeadlinePart3()
							+ ";" + expandedTextAdInfo.getDescription()
							+ ";" + expandedTextAdInfo.getDescription2()
							+ ";" + expandedTextAdInfo.getPath1()
							+ ";" + expandedTextAdInfo.getPath2()
							+ ";" + googleAdsRow.getAdGroupAd().getAd().getFinalUrls(0)
							+ "~";
          }
          
          output = output.substring(0,output.length() - 1);
          return output;
    }
	public static String getrsa(GoogleAdsClient googleAdsClient, long customerId, @Nullable Long adGroupId) throws Throwable {
		/**
		 * This function returns the list of RSAs in an adGroup
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param adGroupId the adGroupId 
		 * 
		 * @returns a string "adgroup_id;ad_id;status;list_of_headlines;list_of_descriptions;path1;path2;finalUrl~
		 * 					  adgroup_id;ad_id;status;list_of_headlines;list_of_descriptions;path1;path2;finalUrl~
		 * 					  ..."
		 */
	    GoogleAdsServiceClient googleAdsServiceClient =
	        googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
	    
	    // Query to fetch required fields from DB
	    String searchQuery =
	          "SELECT ad_group.id, ad_group_ad.ad.id, "
	              + "ad_group_ad.ad.responsive_search_ad.headlines, "
	              + "ad_group_ad.ad.responsive_search_ad.descriptions, "
	              + "ad_group_ad.status, "
	              + "ad_group_ad.ad.final_urls "
	              + "FROM ad_group_ad "
	              + "WHERE ad_group_ad.ad.type = RESPONSIVE_SEARCH_AD "
	              + "AND ad_group_ad.status in ('ENABLED','PAUSED')";
	    if (adGroupId != null) {
	        searchQuery += String.format(" AND ad_group.id = %d", adGroupId);
	    }
	    
	    // Creates a request of the search query
	    SearchGoogleAdsRequest request =
	          SearchGoogleAdsRequest.newBuilder()
						            .setCustomerId(Long.toString(customerId))
						            .setPageSize(PAGE_SIZE)
						            .setQuery(searchQuery)
						            .build();
	    // Iterates over all rows in all pages and prints the requested field values for the ad
        // in each row.
	    SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
	    if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
	        return "";
	    }
	    
	    // Generates an output string
	    String output = "";
	    for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	        AdGroupAd adGroupAd = googleAdsRow.getAdGroupAd();
	        Ad ad = adGroupAd.getAd();
	        ResponsiveSearchAdInfo responsiveSearchAdInfo = ad.getResponsiveSearchAd();
	        
	        // Calling the adTextAssetsToStrings method on headlines list
	        String headlines = adTextAssetsToStrings(responsiveSearchAdInfo.getHeadlinesList());
	        
	        // Calling the adTextAssetsToStrings method on description list
	        String descriptions = adTextAssetsToStrings(responsiveSearchAdInfo.getDescriptionsList());
	        
	        // Creates a list of headlines from the string created by adTextAssetsToStrings
	        List<String> list_of_headlines = new ArrayList<>();
	        list_of_headlines.addAll(Arrays.asList(headlines.split(",")));
	        
	        // Creates a list of descriptions from the string created by adTextAssetsToStrings
	        List<String> list_of_description = new ArrayList<>();
	        list_of_description.addAll(Arrays.asList(descriptions.split(",")));
	        
	        output += adGroupId.toString() + ";"
	        		+ ad.getId() + ";"
	        		+ googleAdsRow.getAdGroupAd().getStatus() + ";";
	        
	        // Append the headlines in the output string
	        for(String headline: list_of_headlines) {
	        	output += headline + ";";
	        } 
	        
	        // Append the descriptions in the output string	
	        for(String description: list_of_description) {
	        	output += description + ";";
	        }
	        output += responsiveSearchAdInfo.getPath1() + ";";
	        output += responsiveSearchAdInfo.getPath2() + ";";
	        output += googleAdsRow.getAdGroupAd().getAd().getFinalUrls(0) + "~";
	      }
	      
	      // Removes the ~ on last index
	      output = output.substring(0,output.length() - 1);
	      return output;
	}
	private static String adTextAssetsToStrings(List<AdTextAsset> adTextAssets) throws Throwable {
		/**
		 * This function consumes a list and returns a string with comma separated values
		 * 
		 * @param adTextAssets a adTextAsset returned from google ads api
		 * @returns a string with comma separated values
		 * 
		 */
	    return adTextAssets.stream()
	        .map(
	            adTextAsset ->
	                adTextAsset.getText())
	        .collect(Collectors.joining(", "));
	}
	
	
	public static String syncETAStatus(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable {
		/**
		 * This function sync the ETA status 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adGroupId;eta_id~
		 * 									adGroupId;eta_id~
		 * 									..."
		 * 
		 * @returns a string "adGroupId;eta_id;status~
		 * 					  adGroupId;eta_id;status~
		 * 					  ..."
		 */
		
		
		// Separates adGroupId and etaId from the data string
	    Long adGroupId = Long.parseLong(data.split("~")[0]);
	    Long etaId = Long.parseLong(data.split("~")[1]);
	    
		GoogleAdsServiceClient googleAdsServiceClient =
	            googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		
		
		// Search Query to extract the status of ETA
	    String searchQuery = "SELECT ad_group_ad.status,"
	    		+ " ad_group_ad.ad.id"
	    		+ " FROM ad_group_ad"
	    		+ " WHERE ad_group_ad.ad.type = 'EXPANDED_TEXT_AD' ";
        if (adGroupId != null) {
            searchQuery += String.format("AND ad_group.id = %d", adGroupId);
        }
        if (etaId != null) {
            searchQuery += String.format(" AND ad_group_ad.ad.id = %d", etaId);
        }
          // Creates a request that will retrieve all ads using pages of the specified page size.
        SearchGoogleAdsRequest request =
              SearchGoogleAdsRequest.newBuilder()
                  .setCustomerId(Long.toString(customerId))
                  .setPageSize(PAGE_SIZE)
                  .setQuery(searchQuery)
                  .build();
          // Issues the search request.
        SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
          // Iterates over all rows in all pages and prints the requested field values for the ad
          // in each row.
        String output = "";
        for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	        Ad ad = googleAdsRow.getAdGroupAd().getAd();
	        output = output + adGroupId.toString() 
	        				+ ";" + ad.getId()
	        				+ ";" + googleAdsRow.getAdGroupAd().getStatus()
							+ "~";
        }
          
	    output = output.substring(0,output.length() - 1);
	    return output;
    }
	@SuppressWarnings("unused")
	public static String syncRSAStatus(GoogleAdsClient googleAdsClient, long customerId, String data) throws Throwable {
		/**
		 * This function sync the RSA status 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param data represents a string "adGroupId;rsa_id~
		 * 									adGroupId;rsa_id~
		 * 									..."
		 * 
		 * @returns a string "adGroupId;rsa_id;status~
		 * 					  adGroupId;rsa_id;status~
		 * 					  ..."
		 */
		
		Long adGroupId = Long.parseLong(data.split("~")[0]);
	    Long rsaId = Long.parseLong(data.split("~")[1]);
	    GoogleAdsServiceClient googleAdsServiceClient =
	        googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();

		// Search Query to extract the status of RSA
	      String searchQuery =
	          "SELECT ad_group.id, ad_group_ad.ad.id, "
	              + "ad_group_ad.status "
	              + "FROM ad_group_ad "
	              + "WHERE ad_group_ad.ad.type = RESPONSIVE_SEARCH_AD ";
	      if (adGroupId != null) {
	        searchQuery += String.format(" AND ad_group.id = %d", adGroupId);
	      }
	      
	      if (rsaId != null) {
		        searchQuery += String.format(" AND ad_group_ad.ad.id = %d", rsaId);
		  }
	      SearchGoogleAdsRequest request =
	          SearchGoogleAdsRequest.newBuilder()
						            .setCustomerId(Long.toString(customerId))
						            .setPageSize(PAGE_SIZE)
						            .setQuery(searchQuery)
						            .build();

          // Creates a request that will retrieve all ads using pages of the specified page size.
	      SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
	      if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
	        return "";
	      }
	      
	      // Iterates over all rows in all pages and prints the requested field values for the ad
          // in each row.
	      String output = "";
	      for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	        AdGroupAd adGroupAd = googleAdsRow.getAdGroupAd();
	        Ad ad = adGroupAd.getAd();
	        ResponsiveSearchAdInfo responsiveSearchAdInfo = ad.getResponsiveSearchAd();
	        output += adGroupId.toString() + ";"
	        		+ ad.getId() + ";"
	        		+ googleAdsRow.getAdGroupAd().getStatus() + ";";
	      }
	      
	      output = output.substring(0,output.length() - 1);
	      return output;
	}
	
	public static Long createAccount(GoogleAdsClient googleAdsClient, String Name, String CurrencyCode, String TimeZone, Long managerId) throws Throwable{
		/**
		 * This function creates a client customer account in a manager account.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param Name the name of the customer
		 * @param CurrencyCode the currency code to be used in the account
		 * @param TimeZone the time zone to be observed for the account
		 * @param managerId the id of manager for which this customer account is to be created
		 * 
		 * @returns a string containing the account id of the newly created account
		 * 
		*/
		
		// Creates a customer with the credentials
		Customer customer =
		        Customer.newBuilder()
		        	.setDescriptiveName(Name)
		            .setCurrencyCode(CurrencyCode)
		            .setTimeZone(TimeZone)
		            .build();
	    CustomerServiceClient client =
		        googleAdsClient.getLatestVersion().createCustomerServiceClient();
        CreateCustomerClientResponse response =
		          client.createCustomerClient(managerId.toString(), customer);
	    return Long.parseLong(response.getResourceName());
	}	
	

	
	// ============================== GET Campaign code ========================= // 
	/*
	 public static void GetCampaigns(GoogleAdsClient googleAdsClient, long customerId) {
		    try (GoogleAdsServiceClient googleAdsServiceClient =
		        googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
		      String query = "SELECT campaign.id, campaign.name FROM campaign ORDER BY campaign.id";
		      // Constructs the SearchGoogleAdsStreamRequest.
		      SearchGoogleAdsStreamRequest request =
		          SearchGoogleAdsStreamRequest.newBuilder()
		              .setCustomerId(Long.toString(customerId))
		              .setQuery(query)
		              .build();

		      // Creates and issues a search Google Ads stream request that will retrieve all campaigns.
		      ServerStream<SearchGoogleAdsStreamResponse> stream =
		          googleAdsServiceClient.searchStreamCallable().call(request);

		      // Iterates through and prints all of the results in the stream response.
		      for (SearchGoogleAdsStreamResponse response : stream) {
		        for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
		          System.out.printf(
		              "Campaign with ID %d and name '%s' was found.%n",
		              googleAdsRow.getCampaign().getId(), googleAdsRow.getCampaign().getName());
		        }
		      }
		    }
		  }*/
	// ============================== Add Campaign Code ================================ //
	public static long addCampaign(GoogleAdsClient googleAdsClient, Long clientCustomerId, String Targetingmethod, String Exclusionmethod, String CampaignType, boolean GoogleSearch, boolean SearchNetwork, boolean ContentNetwork, boolean PartnerSearchNetwork, String StartDate, Boolean EnhancedCPC, String TargetCPA, String BidStrategyType, String BidStrategyName, String CampaignName, String budgetName, String BudgetAmount, String budgetDeliveryMethod, String status, String weekDays[], String[] hourMinutes, String delay) throws Throwable
		{	
			/**
			 * This function allows us to add campaign and returns campaign ids
			 * 
			 * @param googleAdsClient the google ad client
			 * @param customerId the client customer id
			 * 					
			 * A String is returned that return "adw_campaignId"
			 */
		
			// Budget setting for camp//
		
			double budgetAmount=Double.parseDouble(BudgetAmount);
			budgetAmount=budgetAmount*1000000;
			BudgetDeliveryMethod deliveryMethod = BudgetDeliveryMethod.valueOf(budgetDeliveryMethod.toUpperCase());
			
			CampaignBudget budget = CampaignBudget.newBuilder()
					.setName(budgetName + System.currentTimeMillis())
					.setStatus(BudgetStatus.ENABLED)
					.setAmountMicros((long) budgetAmount)
					.setExplicitlyShared(false)
					.setDeliveryMethod(deliveryMethod)
					.build();
			
			CampaignBudgetOperation op = CampaignBudgetOperation.newBuilder().setCreate(budget).build();
			CampaignBudgetServiceClient campaignBudgetServiceClient = googleAdsClient.getLatestVersion().createCampaignBudgetServiceClient();	
			MutateCampaignBudgetsResponse response = campaignBudgetServiceClient.mutateCampaignBudgets(Long.toString(clientCustomerId), ImmutableList.of(op));
			String budgetResourceName = response.getResults(0).getResourceName();
	
		////============================////
		    
		    //Networking settings//
		    NetworkSettings networkSettings = NetworkSettings.newBuilder()
			              .setTargetGoogleSearch(GoogleSearch)
			              .setTargetSearchNetwork(SearchNetwork)
			              .setTargetContentNetwork(ContentNetwork)
			              .setTargetPartnerSearchNetwork(PartnerSearchNetwork)
			              .build();
		    
		   // GeoTargetType settings
		   PositiveGeoTargetType Postarget = PositiveGeoTargetType.valueOf(Targetingmethod);
		   NegativeGeoTargetType Negtarget = NegativeGeoTargetType.valueOf(Exclusionmethod);
		   
		   GeoTargetTypeSetting geoTarget =  GeoTargetTypeSetting.newBuilder()
				   .setPositiveGeoTargetType(Postarget)
				   .setNegativeGeoTargetType(Negtarget)
				   .build();
		   
		   ////=========== BIDDING STARTEGY =================////
		   String bidname=BidStrategyName.toUpperCase();
		   String bidtype=BidStrategyType.toUpperCase().replace(" ", "_");
		   
		   BiddingStrategyType biddingStrategytype= BiddingStrategyType.valueOf(bidtype.toString());
		   
		   TargetSpend targetSpend = TargetSpend.newBuilder().build();
		   
		   BiddingStrategy biddingstartegy = BiddingStrategy.newBuilder()
				   .setName(bidname + System.currentTimeMillis())
				   .setType(biddingStrategytype)
				   .setStatus(BiddingStrategyStatus.valueOf("ENABLED"))
				   .setTargetSpend(targetSpend)
				   .setEffectiveCurrencyCode("USD")
				   .build();
		   
		   BiddingStrategyOperation bid_strategy = BiddingStrategyOperation.newBuilder().setCreate(biddingstartegy).build();
		   
		   BiddingStrategyServiceClient bidServiceClient = googleAdsClient.getLatestVersion().createBiddingStrategyServiceClient();	
		   MutateBiddingStrategiesResponse bidresponse = bidServiceClient.mutateBiddingStrategies(Long.toString(clientCustomerId), ImmutableList.of(bid_strategy));
		   String bidResourceName = bidresponse.getResults(0).getResourceName();
	
		   ////=============END BIDDING STRATEGY ================///
		   
		   AdvertisingChannelType advertisingchanneltype = AdvertisingChannelType.valueOf(CampaignType.toUpperCase());
		   
		   String startdate=StartDate;
			if(startdate.trim() == null || startdate.trim()== "" )
			{
				LocalDateTime y = LocalDateTime.now();  
				startdate = y.toString();
				startdate = startdate.substring(0, 10);
			}
		
		   CampaignStatus Campstatus =  CampaignStatus.valueOf(status);
		   
		   ManualCpc BiddingSchemeMcpc = null;
		   Campaign campaign = null;
		  
		   // =========== CAMPAIGN SETTING ============//
		   if(bidtype.equals("MANUAL_CPC"))
			{
			   BiddingSchemeMcpc = ManualCpc.newBuilder()
					   		.setEnhancedCpcEnabled(EnhancedCPC)
					   		.build();
			   campaign = Campaign.newBuilder()
			             .setName(CampaignName)
				             .setStatus(Campstatus)
				             .setStartDate(startdate)
				             .setCampaignBudget(budgetResourceName)
				             .setAdvertisingChannelType(advertisingchanneltype)
				             .setNetworkSettings(networkSettings)
				             .setGeoTargetTypeSetting(geoTarget)
				             .setBiddingStrategy(bidResourceName)
				             .setManualCpc(BiddingSchemeMcpc)
				             .setNetworkSettings(networkSettings)
				             .build(); //changed parital build
			}
		   else if(bidtype.equals("TARGET_CPA"))
			{	
			   double cpa=Double.parseDouble(TargetCPA)*1000000;
			   TargetCpa BiddingSchemeEcpc = TargetCpa.newBuilder()
					   .setTargetCpaMicros((long) cpa)
					   .build();
			   campaign = Campaign.newBuilder()
				          .setName(CampaignName + System.currentTimeMillis())
				          .setStatus(Campstatus)
				          .setStartDate(startdate)
				          .setCampaignBudget(budgetResourceName)
				          .setAdvertisingChannelType(advertisingchanneltype)
				          .setNetworkSettings(networkSettings)
				          .setGeoTargetTypeSetting(geoTarget)
				          .setBiddingStrategy(bidResourceName)
				          .setTargetCpa(BiddingSchemeEcpc)
				          .setNetworkSettings(networkSettings)
				          .build(); //channed to buildpartial
			} 
		   
		   String campaignResourceName = null;
		   Long campaign_id = null;
		    CampaignOperation Camp_operation = CampaignOperation.newBuilder().setCreate(campaign).build();
		    List<CampaignOperation> operations = new ArrayList<>();
			operations.add(Camp_operation);
			
			CampaignServiceClient campaignServiceClient = googleAdsClient.getLatestVersion().createCampaignServiceClient(); 
			MutateCampaignsResponse camp_response = campaignServiceClient.mutateCampaigns(Long.toString(clientCustomerId), operations);
		  
		    for (MutateCampaignResult result : camp_response.getResultsList()) {
		        campaignResourceName = result.getResourceName();
		        String[] camp_outputs= campaignResourceName.split("/");
		        campaign_id = Long.parseLong(camp_outputs[3]);
		    }
		    //===   END CAMPAIGN ===///
		    
		    //========= CAMPAIGN BIDMODIFER ========//
		    
		    CampaignBidModifier campaignBidModifier =
		            CampaignBidModifier.newBuilder()
		                .setCampaign(campaignResourceName)
		                // Makes the bid modifier apply to call interactions.
		                .setInteractionType(
		                    InteractionTypeInfo.newBuilder().setType(InteractionType.CALLS))
		                // Uses the specified bid modifier value.
		                .setBidModifier((float) 0.7)
		                .build();
		    
		    CampaignBidModifierOperation op1 =
		            CampaignBidModifierOperation.newBuilder().setCreate(campaignBidModifier).build();
		    MutateCampaignBidModifiersRequest request =
		            MutateCampaignBidModifiersRequest.newBuilder()
		                .addOperations(op1)
		                .setCustomerId(String.valueOf(clientCustomerId))
		                // Specifies that we want to the request to return the mutated object and not just its
		                // resource name.
		                .setResponseContentType(ResponseContentType.MUTABLE_RESOURCE)
		                .build();
		    CampaignBidModifierServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignBidModifierServiceClient();
		    MutateCampaignBidModifiersResponse response_ =
		            agcServiceClient.mutateCampaignBidModifiers(request);
		    CampaignBidModifier mutableResource = response_.getResults(0).getCampaignBidModifier();
		      System.out.printf(
		          "Created campaign bid modifier with resource_name "
		              + "'%s', criterion ID "
		              + "%d, and bid modifier value "
		              + "%s, under the campaign with "
		              + "resource_name '%s'.%n",
		          mutableResource.getResourceName(),
		          mutableResource.getCriterionId(),
		          mutableResource.getBidModifier(),
		          mutableResource.getCampaign());
		   
			//====///
		   return campaign_id;	
		}
	
	// =======================================Add Location Code =Nisar=============================== //
	public static boolean updateLocation(GoogleAdsClient googleAdsClient, String customerId, String campaignId,String Location) throws Throwable
	{
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		
		System.out.println(campaignResourceName);
		
		LocationInfo li = LocationInfo.newBuilder().setGeoTargetConstant("geoTargetConstants/" + Location).build();
		
		CampaignCriterion builder = MutateOperation.newBuilder()
				.getCampaignCriterionOperationBuilder().getCreateBuilder()
            .setCampaign(campaignResourceName)
            .setType(CriterionType.LOCATION)
            .setLocation(li)
        	.build();
        
        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
        
        MutateCampaignCriteriaRequest Mutate_Camp_asset_request =
        		MutateCampaignCriteriaRequest.newBuilder()
	            .addOperations(o)
	          	.setCustomerId(String.valueOf(customerId)).build();
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(Mutate_Camp_asset_request);
		return true;
	}
	// =======================================Add NegLocation Code PPC1 Nisar================================ //
	public static boolean Negetivelocationfunction(GoogleAdsClient googleAdsClient, String customerId, String campaignId,String Location) throws Throwable
	{
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		
		System.out.println(campaignResourceName);
		
		LocationInfo li = LocationInfo.newBuilder().setGeoTargetConstant("geoTargetConstants/" + Location).build();
		
		CampaignCriterion builder = MutateOperation.newBuilder()
				.getCampaignCriterionOperationBuilder().getCreateBuilder()
            .setCampaign(campaignResourceName)
            .setType(CriterionType.LOCATION)
            .setNegative(true)
            .setLocation(li)
        	.build();
        
        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
        
        MutateCampaignCriteriaRequest Mutate_Camp_asset_request =
        		MutateCampaignCriteriaRequest.newBuilder()
	            .addOperations(o)
	          	.setCustomerId(String.valueOf(customerId)).build();
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(Mutate_Camp_asset_request);
		return true;
	}
	// =======================================Add NegLocation Code PPC3 Nisar================================ //
	public static boolean Negativelocationfunction(GoogleAdsClient googleAdsClient, String customerId, String campaignId,String Location) throws Throwable
	{
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		
		System.out.println(campaignResourceName);
		
		LocationInfo li = LocationInfo.newBuilder().setGeoTargetConstant("geoTargetConstants/" + Location).build();
		
		CampaignCriterion builder = MutateOperation.newBuilder()
				.getCampaignCriterionOperationBuilder().getCreateBuilder()
            .setCampaign(campaignResourceName)
            .setType(CriterionType.LOCATION)
            .setNegative(true)
            .setLocation(li)
        	.build();
        
        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
        
        MutateCampaignCriteriaRequest Mutate_Camp_asset_request =
        		MutateCampaignCriteriaRequest.newBuilder()
	            .addOperations(o)
	          	.setCustomerId(String.valueOf(customerId)).build();
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(Mutate_Camp_asset_request);
		return true;
	}
	// =======================================Add Language Code PPC3 Nisar================================ //
	public static boolean updateLanguage(GoogleAdsClient googleAdsClient, String customerId, String campaignId,String Location) throws Throwable
	{
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		
		System.out.println(campaignResourceName);
		
		LanguageInfo li = LanguageInfo.newBuilder().setLanguageConstant("languageConstants/" + Location).build();
		
		CampaignCriterion builder = MutateOperation.newBuilder()
				.getCampaignCriterionOperationBuilder().getCreateBuilder()
            .setCampaign(campaignResourceName)
            .setType(CriterionType.LANGUAGE)
            .setLanguage(li)
        	.build();
        
        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
        
        MutateCampaignCriteriaRequest Mutate_Camp_asset_request =
        		MutateCampaignCriteriaRequest.newBuilder()
	            .addOperations(o)
	          	.setCustomerId(String.valueOf(customerId)).build();
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(Mutate_Camp_asset_request);
		return true;
	}
	// =======================================Add NegLocation Code PPC3 Nisar ================================ //
	public static String NEG_NEW(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	{
		List<CampaignCriterionOperation> operations = new ArrayList<>();
		String[] aaa = Data.split("~");
		String[] keyword_table_id = new String[aaa.length];
		for(int i = 0 ; i < aaa.length ; i++)
		{
			String[] bbb = aaa[i].split(";");
			
			String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(bbb[0]));
			
			KeywordInfo li = KeywordInfo.newBuilder().setText(bbb[1]).setMatchType(KeywordMatchType.valueOf(bbb[2])).build();
			keyword_table_id[i] = bbb[3];
			CampaignCriterion builder = CampaignCriterion.newBuilder()
					.setCampaign(campaignResourceName)
					.setType(CriterionType.KEYWORD)
					.setNegative(true)
					.setKeyword(li)
					.build();
	        CampaignCriterionOperation o = CampaignCriterionOperation.newBuilder().setCreate(builder).build();
	        operations.add(o);
		}
        
        
        CampaignCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createCampaignCriterionServiceClient();
        MutateCampaignCriteriaResponse response_ = agcServiceClient.mutateCampaignCriteria(customerId,operations);
        int i = 0;
        String response = "null**"; 
        for (MutateCampaignCriterionResult result : response_.getResultsList())
        {
        	response = response + keyword_table_id[i] + ";";
        	String[] v = result.getResourceName().split("/");
        	System.out.println("----*"+result.getResourceName());
        	response = response + v[3].split("~")[1] + "~";
        	i++;
        }
        response = response.substring(0,response.length() - 1);
        response = response.replace("null**", "");
        
        return response;
	}
	
	// ============================== Add Sitelinks Campaign========================= // 
	public static boolean sitelinks(GoogleAdsClient googleAdsClient,  String customerId, String record,String campaignId) throws Throwable
	{	
		/**
		 * This function uploads the list of Sitelink against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to upload the Sitelink Extensions
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of Sitelink against a campaign Id. 
		 * 										"SitelinkText;Line_1;Line_2;Final_url;Status~
		 * 										 SitelinkText;Line_1;Line_2;Final_url;Status~
		 * 										 ...."
		 */
		
		//Campaign Resource name is generated 
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		List<ExtensionFeedItemOperation> operations1 = new ArrayList<>(); //operation list initated
		
		String[] Record_Sitelinks = record.split("~"); //Different Sitelinks seperated by ~
		String[] Sitelink;
		//For each sitelink 
		for(int i = 0; i<Record_Sitelinks.length; i++)
		{
			Sitelink = Record_Sitelinks[i].split(";"); //Sitelink Data obtained
			
			//Sitleink feed item is called and values are set 
			SitelinkFeedItem sitelinkFeedItem1 = SitelinkFeedItem.newBuilder()
												.setLinkText(Sitelink[0]) 			//linktext added
												.setLine1(Sitelink[1]) 				//Line sitelink 1 added
												.setLine2(Sitelink[2]) 				//Line sitelink 2 added
												.addFinalUrls(Sitelink[3])			//Final URL added
												.build(); 
			
			//Extension feed item is called and sitlink is set along with extension type and status
			//==============Mobile Extension Feed ================
			ExtensionFeedItem extensionFeedItem_mobile =ExtensionFeedItem.newBuilder()
					      	  							.setDevice(FeedItemTargetDevice.MOBILE)
					      	  							.setExtensionType(ExtensionType.SITELINK)
					      	  							.setSitelinkFeedItem(sitelinkFeedItem1)
					      	  							.setTargetedCampaign(campaignResourceName)
					      	  							.setStatus(FeedItemStatus.valueOf(Sitelink[4]))
					      	  							.build();
			//==============Extension Feed ================	
			ExtensionFeedItem extensionFeedItem = ExtensionFeedItem.newBuilder()
					      	  .setExtensionType(ExtensionType.SITELINK)
					          .setSitelinkFeedItem(sitelinkFeedItem1)
					          .setTargetedCampaign(campaignResourceName)
					          .setStatus(FeedItemStatus.valueOf(Sitelink[4]))
					          .build();
			//Operation 
			ExtensionFeedItemOperation Sitelink_op = ExtensionFeedItemOperation.newBuilder()
													.setCreate(extensionFeedItem)
													.build();
			//operation is added to operation list
			operations1.add(Sitelink_op);
				
			ExtensionFeedItemOperation Sitelink_op1 = ExtensionFeedItemOperation.newBuilder()
													  .setCreate(extensionFeedItem_mobile)
													  .build();
			operations1.add(Sitelink_op1);
		      
		}
		
		//Operation list is set for mutation for customer id 
		ExtensionFeedItemServiceClient extensionFeedItemServiceClient =
			      googleAdsClient.getLatestVersion().createExtensionFeedItemServiceClient();
		
		MutateExtensionFeedItemsResponse response =
		        extensionFeedItemServiceClient.mutateExtensionFeedItems(customerId, operations1);
		
	    String extensionResourceName = null; 
	    //Result list for mutation are obtained that is its resource name for each sitelink operation
	    for (MutateExtensionFeedItemResult result : response.getResultsList()) 
	    {
	    		extensionResourceName=result.getResourceName(); //Resource name is set for Extension
	    		
	    		//Create campaign link to extension mutated resource
	    		CampaignExtensionSetting campaignExtensionSetting =
	    				CampaignExtensionSetting.newBuilder()
	    				.setCampaign(campaignResourceName) 				//set campaign resoource name
	    				.setExtensionType(ExtensionType.SITELINK) 		//set type to sitelink
	    				.addExtensionFeedItems(extensionResourceName)	//set extension resource name
	    				.build();
	    		
	    		//create an operation for the campaign link to extension
	    		CampaignExtensionSettingOperation operation =CampaignExtensionSettingOperation.newBuilder()
	    													.setCreate(campaignExtensionSetting)
	    													.build();
	    		
	    		CampaignExtensionSettingServiceClient campaignExtensionSettingServiceClient =
	    											googleAdsClient.getLatestVersion()
	    											.createCampaignExtensionSettingServiceClient();
	            //mutate the campaign and extension to create a link
	            MutateCampaignExtensionSettingsResponse response1 =
	            		campaignExtensionSettingServiceClient
	            		.mutateCampaignExtensionSettings(customerId, ImmutableList.of(operation));
	       
	           /* for (MutateCampaignExtensionSettingResult result1 : response1.getResultsList()) {
	          			System.out.printf(
	              			"Created CampaignExtensionSetting with resource name '%s'.%n",
	              		result.getResourceName());
	              }*/
	    }
	    return true;
	}
	
	// ============================== Add Callout Campaign========================= // 
	public static boolean Callout(GoogleAdsClient googleAdsClient,  String customerId, String campaignId, String record) throws Throwable
	{
		/**
		 * This function uploads the list of Callout against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to upload the Callout Extensions
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of Callout against a campaign Id. 
		 * 										"CalloutText;Status~
		 * 										 CalloutText;Status~
		 * 										 ...."
		 */
		
		//Campaign Resource Name  
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		
		List<ExtensionFeedItemOperation> operations1 = new ArrayList<>(); //operation list for mutation
		
		String[] Callout_datas = record.split("~"); //Different Callouts
		String[] Callout;
		//Iterate and for each callout set feed item 
		for(int i = 0; i<Callout_datas.length; i++)
		{
			Callout = Callout_datas[i].split(";");
			//CALLOUT  feed item is called and values are set 
			CalloutFeedItem CalloutFeedItem1 = CalloutFeedItem.newBuilder()
												.setCalloutText(Callout[0]) //callout text added
												.build();
			
			//Extension feed item is called and CALLOUT is set along with extension type and status
			//==============Mobile Extension Feed ================
			ExtensionFeedItem extensionFeedItem_mobile =ExtensionFeedItem.newBuilder()
				      	  						.setDevice(FeedItemTargetDevice.MOBILE)
				      	  						.setExtensionType(ExtensionType.CALLOUT)
				      	  						.setCalloutFeedItem(CalloutFeedItem1)
				      	  						.setTargetedCampaign(campaignResourceName)
				      	  						.setStatus(FeedItemStatus.valueOf(Callout[1]))
				      	  						.build();
			//==============Extension Feed ================	
			ExtensionFeedItem extensionFeedItem = ExtensionFeedItem.newBuilder()
												.setExtensionType(ExtensionType.CALLOUT)
												.setCalloutFeedItem(CalloutFeedItem1)
												.setTargetedCampaign(campaignResourceName)
												.setStatus(FeedItemStatus.valueOf(Callout[1]))
												.build();
			//Operation Mobile
			ExtensionFeedItemOperation calout_op1 = ExtensionFeedItemOperation.newBuilder()
					.setCreate(extensionFeedItem_mobile).build();
			//Operation Added
			operations1.add(calout_op1);
			//Operation 
			ExtensionFeedItemOperation calout_op = ExtensionFeedItemOperation.newBuilder()
					.setCreate(extensionFeedItem).build();
			//Operation Added
			operations1.add(calout_op);
			
		}
			
		ExtensionFeedItemServiceClient extensionFeedItemServiceClient =
				      googleAdsClient.getLatestVersion().createExtensionFeedItemServiceClient();
	
		//Mutate the operation list of Callouts
		MutateExtensionFeedItemsResponse response =
			        extensionFeedItemServiceClient.mutateExtensionFeedItems(customerId, operations1);
		    
		String extensionResourceName = null; 
		for (MutateExtensionFeedItemResult result : response.getResultsList()) {
		      //For each Call out get the resource name generated after mutation
			  extensionResourceName=result.getResourceName();
		      //Set a link between campaign extensionn and extension feed callout 
		      CampaignExtensionSetting campaignExtensionSetting =CampaignExtensionSetting.newBuilder()
		    		  									.setCampaign(campaignResourceName)
		    		  									.setExtensionType(ExtensionType.CALLOUT)
		    		  									.addExtensionFeedItems(extensionResourceName)
		    		  									.build();
		      //operation created
		      CampaignExtensionSettingOperation operation =
		            CampaignExtensionSettingOperation.newBuilder().setCreate(campaignExtensionSetting).build();
		      
		      CampaignExtensionSettingServiceClient campaignExtensionSettingServiceClient =
		            googleAdsClient.getLatestVersion().createCampaignExtensionSettingServiceClient();
		      //mutate the operation 
		      MutateCampaignExtensionSettingsResponse response1 =campaignExtensionSettingServiceClient
		    		  .mutateCampaignExtensionSettings(customerId, ImmutableList.of(operation));
		        
		      for (MutateCampaignExtensionSettingResult result1 : response1.getResultsList()) {
		              result1.getResourceName();
		        }
		      }
		return true;
	}
	
	// ============================== Add StructuredSnippets Campaign========================= // 
	public static boolean StructuredSnippets(GoogleAdsClient googleAdsClient,  String customerId, String campaignId, String record) throws Throwable
	{	
		/**
		 * This function uploads the list of StucturedSnippet against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to upload the StucturedSnippet Extensions
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of StucturedSnippet against a campaign Id. 
		 * 										"Header;Value1,value2;Status~
		 * 										 Header;Value1,value2;Status~
		 * 										 ...."
		 */
		
		String campaignResourceName = ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(campaignId));
		List<ExtensionFeedItemOperation> operations1 = new ArrayList<>();
		String[] SS_datas = record.split("~");
		String[] SSnippet;
		for(int i = 0; i<SS_datas.length; i++)
		{
			SSnippet = SS_datas[i].split(";");
			Iterable<String> iterable = Arrays.asList(SSnippet[1].split(","));
			StructuredSnippetFeedItem SS_Feeditem =StructuredSnippetFeedItem.newBuilder()
						.setHeader(SSnippet[0]).addAllValues(iterable).build();	
			//Extension Feed item setup for Mobile
			ExtensionFeedItem Destination_SS_Mobile =
					      ExtensionFeedItem.newBuilder()
					      	  .setDevice(FeedItemTargetDevice.MOBILE)
					          .setExtensionType(ExtensionType.STRUCTURED_SNIPPET)
					          .setStructuredSnippetFeedItem(SS_Feeditem)
					          .setTargetedCampaign(campaignResourceName)
					          .setStatus(FeedItemStatus.valueOf(SSnippet[2].toUpperCase()))
					          .build();
			//Extension Feed item setup
			ExtensionFeedItem Destination_SS =
					      ExtensionFeedItem.newBuilder()
					      	  .setExtensionType(ExtensionType.STRUCTURED_SNIPPET)
					          .setStructuredSnippetFeedItem(SS_Feeditem)
					          .setTargetedCampaign(campaignResourceName)
					          .setStatus(FeedItemStatus.valueOf(SSnippet[2].toUpperCase()))
					          .build();
			//Operation addition	
			ExtensionFeedItemOperation SS_op1 = ExtensionFeedItemOperation.newBuilder()
						.setCreate(Destination_SS_Mobile).build();
			operations1.add(SS_op1);
	
			ExtensionFeedItemOperation SS_op = ExtensionFeedItemOperation.newBuilder()
						.setCreate(Destination_SS).build();
			operations1.add(SS_op);		
				
		}
	
		ExtensionFeedItemServiceClient extensionFeedItemServiceClient =
			      googleAdsClient.getLatestVersion().createExtensionFeedItemServiceClient();
		MutateExtensionFeedItemsResponse response =
		        extensionFeedItemServiceClient.mutateExtensionFeedItems(customerId, operations1);
		
	    String extensionResourceName = null;
	    
	    for (MutateExtensionFeedItemResult result : response.getResultsList()) {
	    	extensionResourceName=result.getResourceName();
	    
	    	CampaignExtensionSetting campaignExtensionSetting =CampaignExtensionSetting.newBuilder()
	    														.setCampaign(campaignResourceName)
	    														.setExtensionType(ExtensionType.STRUCTURED_SNIPPET)
	    														.addExtensionFeedItems(extensionResourceName)
	    														.build();
	    
	    	CampaignExtensionSettingOperation operation = CampaignExtensionSettingOperation.newBuilder()
	    													.setCreate(campaignExtensionSetting)
	    													.build();
	   
	    	CampaignExtensionSettingServiceClient campaignExtensionSettingServiceClient =googleAdsClient.getLatestVersion()
	    			.createCampaignExtensionSettingServiceClient();
	            
	    	MutateCampaignExtensionSettingsResponse response1 =campaignExtensionSettingServiceClient.mutateCampaignExtensionSettings(customerId, ImmutableList.of(operation));
	        
	    	for (MutateCampaignExtensionSettingResult result1 : response1.getResultsList()) {
	              result1.getResourceName();
	        }
	    }
	    return true;
	}		
	
	// ============================== create_labels Campaign========================= // 
	public static String create_labels(GoogleAdsClient googleAdsClient,  String customerId, String Data) throws Throwable
	{   
		/**
		 * This function uploads the list of labels.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param Data a string with ; separated values and ~ separated records of StucturedSnippet against a campaign Id. 
		 * 										"LabelName1;LabelName2;LabelName3;LabelName4;LabelName5~
		 * 										 LabelName1;LabelName2;LabelName3;LabelName4;LabelName5~
		 * 										 ...."
		 * @Return String; A string with ; seperated values and ~ sepertaed records of labels 
		 * 										"LabelName1; Label_id~
		 * 									     LabelName1; Label_id~
		 * 										 LabelName1; Label_id~
		 *                                       ...."
		 */
		
		String[] Labels = Data.split("~"); //a~b~c
		String data = "";
		String Labelnames= null + ";";
		List<LabelOperation> operations = new ArrayList<>();
		
		for(int i = 0; i<Labels.length; i++) 
		{
			Label Label_account = Label.newBuilder().setName(Labels[i]).build();
			Labelnames = Labelnames + Labels[i] + ";";
			LabelOperation las = LabelOperation.newBuilder().setCreate(Label_account).build();
			operations.add(las);
		}
		
		LabelServiceClient labelseviceclient = googleAdsClient.getLatestVersion().createLabelServiceClient();
		
		MutateLabelsResponse responce1 = labelseviceclient.mutateLabels(customerId, operations);
		
		int a= 1;
		for( MutateLabelResult result : responce1.getResultsList()) {
			String [] out = null;
			String [] out1 = null;
			out1 = Labelnames.split(";");
			out= result.getResourceName().split("/");
			data = data + out[3] + ";";
			data = data + out1[a]+ "~";
			a++;
		}
		
		data = data.substring(0,data.length() - 1);
		return data;
	}
	
	// ============================== Add Adgroup ========================= //
	public static String addAdGroup(GoogleAdsClient googleAdsClient, String customerId, String data) {
	    List<String> list_of_groups = new ArrayList<>();
	    list_of_groups.addAll(Arrays.asList(data.split("~")));
	    String output = "null**";
	    long[] table_ids = new long[list_of_groups.size()];
	    List<AdGroupOperation> operations = new ArrayList<>();
	    for(int i = 0; i < list_of_groups.size(); i++) {
	    	String[] values = list_of_groups.get(i).split(";");
	    	String campaignResourceName = ResourceNames.campaign( Long.parseLong(customerId), Long.parseLong(values[0]));
	    	String adw_adgroup_name = values[1];
	    	String status = values[2];
	    	float max_cpc1 = Float.parseFloat(values[3]);
	    	long max_cpc =  (long) (max_cpc1 * 1000000);
	    	String rotation = values[4];
	    	String table_id = values[5];
	    	
	    	
	    	table_ids[i] = Long.parseLong(table_id);
	    	AdGroup adGroup = AdGroup.newBuilder()
    								 .setName(adw_adgroup_name)
    								 .setStatus(AdGroupStatus.valueOf(status))
	    							 .setCampaign(campaignResourceName)
	    							 .setCpcBidMicros(max_cpc)
	    							 .setAdRotationMode(AdGroupAdRotationMode.valueOf(rotation))
	    							 .build();
	    	AdGroupOperation Camp_operation = AdGroupOperation.newBuilder().setCreate(adGroup).build();
			operations.add(Camp_operation);
			
	    }
	    
	    AdGroupServiceClient adGroupServiceClient = googleAdsClient.getLatestVersion().createAdGroupServiceClient(); 
		MutateAdGroupsResponse adGroup_response = adGroupServiceClient.mutateAdGroups(String.valueOf(customerId), operations);
		System.out.printf("Added %d Adgroups:%n", adGroup_response.getResultsCount());
		int index = 0;
	    for (MutateAdGroupResult result : adGroup_response.getResultsList()) {
	    	output += table_ids[index];
	    	output += ";";
	    	output += result.getResourceName().split("/")[3];
	        output += "~";
	        index+=1;
	    }
	    output = output.replace("null**", "");
	    output = output.substring(0,output.length() - 1);
	    return output;
	}
	
	// ============================== Add Keyword ========================= //
	public static String addKeywords(GoogleAdsClient googleAdsClient, String customerId,  String data) {
		List<AdGroupCriterionOperation> operations = new ArrayList<>();
		List<String> list_of_keywords = new ArrayList<>();
	    list_of_keywords.addAll(Arrays.asList(data.split("~")));
	    String output = "null**";
	    long[] keyword_ids = new long[list_of_keywords.size()];

	    for(int i = 0; i < list_of_keywords.size(); i++) {
	    	String[] values = list_of_keywords.get(i).split(";");
	    	
	    	String adGroupId = values[0];
	    	String keywordId = values[1];
	    	String keywordText = values[2];
	    	String finalUrl = values[3];
	    	String matchType = values[4];
	    	String status = values[5];
	    	
	    	keyword_ids[i] = Long.parseLong(keywordId);
	    	
	    	KeywordInfo keywordInfo = KeywordInfo.newBuilder()
	    										 .setText(keywordText)
	    										 .setMatchType(KeywordMatchType.valueOf(matchType))
	    										 .build();
	    
	    	 String adGroupResourceName = ResourceNames.adGroup(Long.parseLong(customerId), Long.parseLong(adGroupId));
	    	 
	    	 AdGroupCriterion criterion =
	 		        AdGroupCriterion.newBuilder()
	 		            .setAdGroup(adGroupResourceName)
	 		            .setStatus(AdGroupCriterionStatus.valueOf(status))
	 		            .setKeyword(keywordInfo)
	 		            .addFinalUrls(finalUrl)
	 		            .build();
	    	 
	    	 AdGroupCriterionOperation op =
	 		        AdGroupCriterionOperation.newBuilder().setCreate(criterion).build();
	    	 
	    	 operations.add(op);
	     }
	    AdGroupCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createAdGroupCriterionServiceClient();
	    
	    MutateAdGroupCriteriaResponse response = agcServiceClient.mutateAdGroupCriteria(customerId, operations);
	    int index = 0;
	      for (MutateAdGroupCriterionResult result : response.getResultsList()) {
	        
	        output += keyword_ids[index];
			output += ";";
			output += result.getResourceName().split("~")[1];
			output += "~";
				index+=1;
			System.out.println(result.getResourceName());
	      }
	      return output;
	}
	
	// ============================== add_keyword_labels ========================= //
	public static String add_keyword_labels(GoogleAdsClient googleAdsClient,  String customerId, String str) throws Throwable
	{	
		/**
		 * This function uploads the list of adw_keyword_id. adw_label_id usig the uploaded id we link them both 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of Sitelink against a campaign Id. 
		 * 										"adw_adGroup_id;adW_keyword_id;adw_label_id;labelname~
		 * 										 adw_adGroup_id;adW_keyword_id;adw_label_id~
		 * 										 ...."
		 * A String is returned that return labelname;id_linkded_keyword and label 
		 */
		String[] Each_input;
		String[] Input_datas = str.split("~");
		
		List<AdGroupCriterionLabelOperation> operations = new ArrayList<>();
		String[] arr = new String[Input_datas.length];
		String data = "";
		
		for(int i = 0; i<Input_datas.length; i++)
		{
			Each_input = Input_datas[i].split(";");
			
			String adGroupId= Each_input[0];
			String keywordId=Each_input[1];
			String LabelId=Each_input[2];
			arr[i] = Each_input[3];
			
			String AdGroupCriterionName=ResourceNames.adGroupCriterion(Long.parseLong(customerId), Long.parseLong(adGroupId), Long.parseLong(keywordId));
			String LabelName= ResourceNames.label(Long.parseLong(customerId), Long.parseLong(LabelId));
			
			AdGroupCriterionLabel adgrouplabel = AdGroupCriterionLabel.newBuilder()
					.setAdGroupCriterion(AdGroupCriterionName) //customers/{customer_id}/adGroupCriteria/{ad_group_id}~{criterion_id}
					.setLabel(LabelName)
					.build();
			
			AdGroupCriterionLabelOperation operation = AdGroupCriterionLabelOperation.newBuilder()
					.setCreate(adgrouplabel)
					.build();
			operations.add(operation);	
		}	
		
		MutateAdGroupCriterionLabelsRequest mutaterequest = MutateAdGroupCriterionLabelsRequest.newBuilder().setCustomerId(customerId).addAllOperations(operations).build();
		
		AdGroupCriterionLabelServiceClient adGroupAdServiceClient = googleAdsClient.getLatestVersion().createAdGroupCriterionLabelServiceClient();
		
		MutateAdGroupCriterionLabelsResponse mutatedlabel =  adGroupAdServiceClient.mutateAdGroupCriterionLabels(mutaterequest);
		
		int i = 0;
		for( MutateAdGroupCriterionLabelResult result : mutatedlabel.getResultsList()) 
		{	
			data = data + arr[i] + ";";
			data = data + result.getResourceName().split("~")[2] + "~";
			i++;
		}

		data = data.substring(0,data.length() - 1);
		return data;
	}
		// ============================== NEGATIVE KEYWORD FUNCTION ========================= //

	// ============================== Negative Keyword Function New ========================= //
	public static String  Negativekeywordfunction_new(GoogleAdsClient googleAdsClient,  String customerId, String Data) throws Throwable
	{
		/**
		 * This function returns the list of negative keywords against a AdgroupId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of negative keywords against a AdgroupId. 
		 * 										"Adgroupid;NegKeyword; MatchType~
		 * 										 Adgroupid;NegKeyword; MatchType~
		 * 										 ...."
		 */
		
		String[] AdGroup_NegKeyword = Data.split("~");
		String output = "";
		String[] keyword_ids = new String[AdGroup_NegKeyword.length];
	    List<AdGroupCriterionOperation> operations = new ArrayList<>();
	    
	    for(int i = 0; i<AdGroup_NegKeyword.length; i++)
		{
			String[] AdGroup_NegKeyword_ids= AdGroup_NegKeyword[i].split(";");
			keyword_ids[i] = AdGroup_NegKeyword_ids[3];
			String AdgroupId= AdGroup_NegKeyword_ids[0];
			String keywordText =AdGroup_NegKeyword_ids[1];
			String matchType = AdGroup_NegKeyword_ids[2];
			
			String adGroupResourceName = ResourceNames.adGroup(Long.parseLong(customerId), Long.parseLong(AdgroupId));
			 
			KeywordInfo keywordInfo = KeywordInfo.newBuilder()
					 .setText(keywordText)
					 .setMatchType(KeywordMatchType.valueOf(matchType))
					 .build();
			
			AdGroupCriterion Adgroupnegcriter = AdGroupCriterion.newBuilder()
					.setNegative(true)
					.setAdGroup(adGroupResourceName)
					.setKeyword(keywordInfo)
					.build();
			AdGroupCriterionOperation AdgroupOperation = AdGroupCriterionOperation.newBuilder()
					.setCreate(Adgroupnegcriter).build();
			operations.add(AdgroupOperation);
		}
	    
	    AdGroupCriterionServiceClient agcServiceClient = googleAdsClient.getLatestVersion().createAdGroupCriterionServiceClient();
	    
	    MutateAdGroupCriteriaResponse response = agcServiceClient.mutateAdGroupCriteria(customerId, operations);
	    
	    int index = 0;
	    for (MutateAdGroupCriterionResult result : response.getResultsList()) 
	    {
	        output += keyword_ids[index];
			output += ";";
			output += result.getResourceName().split("~")[1];
			output += "~";
			index+=1;
	      }
	    
	    output = output.substring(0,output.length() - 1);
	    return output;		
	}

	// ============================== GET NEGATIVE KEYWORD FUNCTION ========================= //
	public static String getnegkeyword(GoogleAdsClient googleAdsClient,  String customerId, String CampId) throws Throwable
	{ 
		/**
		 * This function returns the list of Negative Keywords against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the Negative Keywords
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of Negative Keywords against a campaign Id. 
		 * 										"CampaignId;Keyword_text;keyword_matchtype;keyword_id~
		 * 										 CampaignId;Keyword_text;keyword_matchtype;keyword_id~
		 * 										 ...."
		 */
		int PAGE_SIZE = 100;
		String out = "";
		
		GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		//Search query to obtain the negative keywords 
		String searchQuery ="SELECT campaign_criterion.criterion_id, "
								+ "campaign_criterion.keyword.text, "
								+ "campaign_criterion.keyword.match_type "
								+ "FROM campaign_criterion "
								+ "WHERE campaign_criterion.negative = TRUE "
								+ "AND campaign_criterion.type = 'KEYWORD'";
		
		if (CampId != null) 
		{
		   searchQuery += String.format(" AND campaign.id= %s",CampId);
		}
		String next_page = ""; //page logic is implemented 
		
		do  //Run a while loop until next page token is not obtained 
		{	
			/** We are using page token logic in this function. A campaign can have a more than 10K keywords
			 * the max limit to recieve keywords is 10K at time. Say we Want to get 50K keywords for the said campaign 
			 * if page token logic is not implemented we will only get 10K keywords and 40K keywords would not be obtained.
			 * Page token allows us to get 10K pages and then generated next page token, to obtain the next 10K pages and so on. 
			 * until the next token is null. This concept is similar to linked list.
			 * 
			 */
			
			SearchGoogleAdsRequest request = SearchGoogleAdsRequest.newBuilder()
							            .setCustomerId(customerId)
							            .setPageSize(PAGE_SIZE)
							            .setPageToken(next_page) //page token set
							            .setQuery(searchQuery)
							            .build();
		
			SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
			next_page = searchPagedResponse.getNextPageToken(); //Next page token is obtained 
			
			if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) 
				{
					return "";
				}
		
			SearchPage searchPage = searchPagedResponse.getPage(); //instead of iterate all we use getpage()
			for (GoogleAdsRow googleAdsRow : searchPage.getValues())
				{ 
					out = out + CampId + ";";																//Campaign Id
					out = out + googleAdsRow.getCampaignCriterion().getKeyword().getText() + ";"; 			//Keyword Text
					out = out + googleAdsRow.getCampaignCriterion().getKeyword().getMatchType() + ";"; 		//Keyword Match type
					out = out + googleAdsRow.getCampaignCriterion().getCriterionId() + "~";					//Keyword Id
				}
			}while(!next_page.equals(""));
		out = out.substring(0,out.length() - 1);
		return out;
	}

	// ============================== GET Location FUNCTION ========================= //
	public static String getlocation(GoogleAdsClient googleAdsClient,  String customerId, String CampId) throws Throwable
	{	
		/**
		 * This function returns the list of Geo-targeting location against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the geo targeting locations of the campaign.
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of targeting location against a campaign Id. 
		 * 										"CampaignId;CodeLocation;locationName~
		 * 										 CampaignId;CodeLocation;locationName~
		 * 										 ...."
		 */
		
		int PAGE_SIZE = 10000;
		String Location_ids = "";
		String ReturnData = "";
		
		GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		
		//Search Query to obtain the geo target locations id 
		String searchQuery ="SELECT campaign_criterion.criterion_id, "
							+ "campaign_criterion.location.geo_target_constant, "
							+ "campaign_criterion.location_group,"
							+" campaign_criterion.location_group.feed"
							+" FROM campaign_criterion"
							+" WHERE campaign_criterion.negative = FALSE"
							+" AND campaign_criterion.type ='LOCATION'";
		
		if (CampId != null) 
		{
		   searchQuery += String.format(" AND campaign.id= %s",CampId);
		}		
		
		//API requested with the location ids 
		SearchGoogleAdsRequest request =SearchGoogleAdsRequest.newBuilder()
							            .setCustomerId(customerId)
							            .setPageSize(PAGE_SIZE)
							            .setQuery(searchQuery)
							            .build();
		
		//API response for the location ids are obtained 
		SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
		
		if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) 
			{
				return "";
			}
		//Iterate over the results obtained 
		for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll())
			{	
				CampaignCriterion CampLoc = googleAdsRow.getCampaignCriterion(); //Campaing citerion class 
				ReturnData =ReturnData + CampLoc.getResourceName().split("/")[3].split("~")[0] + ";"; 				//Obtained campaign id from the resource name
				Location_ids = Location_ids + CampLoc.getLocation().getGeoTargetConstant().split("/")[1] + "," ; 	//Created a comma seperated id of location 
																													//    123,456,5677
			}
		
		Location_ids =Location_ids.substring(0,Location_ids.length() - 1); //Removing the last , form the location id list
		
		ReturnData =ReturnData + Location_ids + ";"; //Added the list to return data
		
		//Search query to obatiend the location ids name and code 
		String searchQuery_Location ="SELECT geo_target_constant.name, "
									+ "geo_target_constant.id, "
									+ "geo_target_constant.country_code, "
									+ "geo_target_constant.canonical_name, "
									+ "geo_target_constant.parent_geo_target, "
									+ "geo_target_constant.resource_name, "
									+ "geo_target_constant.status, "
									+ "geo_target_constant.target_type "
									+ "FROM geo_target_constant "
									+ "WHERE geo_target_constant.id IN (" + Location_ids + ")";
		
		SearchGoogleAdsRequest request_location =SearchGoogleAdsRequest.newBuilder()
												.setCustomerId(customerId)
												.setPageSize(PAGE_SIZE)
												.setQuery(searchQuery_Location)
												.build();

		SearchPagedResponse searchPagedResponse_location = googleAdsServiceClient.search(request_location);


		if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) 
		{
			return "";
		}
		
		for (GoogleAdsRow googleAdsRow_location : searchPagedResponse_location.iterateAll())
		{	
			ReturnData = ReturnData + (googleAdsRow_location.getGeoTargetConstant().getName()) + "~";  //Return Data location name added
		}
			ReturnData =ReturnData.substring(0,ReturnData.length() - 1); //Removed the ~ from the string from the end 
			return ReturnData;
		}

	// ============================== GET Negative Location FUNCTION ========================= //
	public static String getneglocation(GoogleAdsClient googleAdsClient,  String customerId, String CampId) throws Throwable
	{   
		/**
		 * This function returns the list of Geo-targeting NEGATIVE location  against a campaignId.
		 * 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the geo targeting NEGATIVE locations of the campaign.
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of targeting NEGATIVE location against a campaign Id. 
		 * 										"CampaignId;CodeLocation;locationName~
		 * 										 CampaignId;CodeLocation;locationName~
		 * 										 ...."
		 */
			int PAGE_SIZE = 10000;
			String Location_ids = "";
			String ReturnData = "";
			
			GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
			 
			String searchQuery ="SELECT campaign_criterion.criterion_id, "
								+ "campaign_criterion.location.geo_target_constant, "
								+ "campaign_criterion.location_group,"
								+" campaign_criterion.location_group.feed"
								+" FROM campaign_criterion"
								+" WHERE campaign_criterion.negative = TRUE"
								+" AND campaign_criterion.type ='LOCATION'";
			
			if (CampId != null) 
			{
			   searchQuery += String.format(" AND campaign.id= %s",CampId);
			}		
		
			SearchGoogleAdsRequest request =SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId(customerId)
								            .setPageSize(PAGE_SIZE)
								            .setQuery(searchQuery)
								            .build();
			
			SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
			
			if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) 
				{
					return "";
				}
			
			for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll())
				{	
					CampaignCriterion CampLoc = googleAdsRow.getCampaignCriterion();
					ReturnData =ReturnData + CampLoc.getResourceName().split("/")[3].split("~")[0] + ";";
					Location_ids = Location_ids + CampLoc.getLocation().getGeoTargetConstant().split("/")[1] + "," ;
				}
			
			Location_ids =Location_ids.substring(0,Location_ids.length() - 1);
			
			ReturnData =ReturnData + Location_ids + ";";
		
			String searchQuery_Location ="SELECT geo_target_constant.name, "
										+ "geo_target_constant.id, "
										+ "geo_target_constant.country_code, "
										+ "geo_target_constant.canonical_name, "
										+ "geo_target_constant.parent_geo_target, "
										+ "geo_target_constant.resource_name, "
										+ "geo_target_constant.status, "
										+ "geo_target_constant.target_type "
										+ "FROM geo_target_constant "
										+ "WHERE geo_target_constant.id IN (" + Location_ids + ")";
			
			
			SearchGoogleAdsRequest request_location =SearchGoogleAdsRequest.newBuilder()
		            									.setCustomerId(customerId)
		            									.setPageSize(PAGE_SIZE)
		            									.setQuery(searchQuery_Location)
		            									.build();

			SearchPagedResponse searchPagedResponse_location = googleAdsServiceClient.search(request_location);


			if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) 
			{
				return "";
			}
			
			for (GoogleAdsRow googleAdsRow_location : searchPagedResponse_location.iterateAll())
			{	
				ReturnData = ReturnData + (googleAdsRow_location.getGeoTargetConstant().getName()) + "~";
			}
			ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
				return ReturnData;
			}

	// ============================== GET Sitelink FUNCTION ========================= //
	public static String getSitelink(GoogleAdsClient googleAdsClient, String customerId, String campaignId) throws Throwable
	{	
		/**
		 * This function returns the list of Sitelinks against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the Sitelink Extensions
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of Sitelinks against a campaign Id. 
		 * 										"CampaignId;Sitelink_Text;Desciption1;Description2;Final_Url; Status~
		 * 										 CampaignId;Sitelink_Text;Desciption1;Description2;Final_Url; Status~"
		 * 										 ...."
		 */
		int PAGE_SIZE = 10000;
	    GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
	    
	    //Select Query 
	    String searchQuery = "SELECT campaign_extension_setting.extension_feed_items "
	    		+ "FROM campaign_extension_setting "
	    		+ "WHERE campaign_extension_setting.extension_type = 'SITELINK'";
	    
	    if (campaignId != null) {
	        searchQuery += String.format(" AND campaign.id = %s", campaignId);
	    }
	   //Select Request with Page_size and customerId
	    SearchGoogleAdsRequest request =SearchGoogleAdsRequest.newBuilder()
							            .setCustomerId((customerId))
							            .setPageSize(PAGE_SIZE)
							            .setQuery(searchQuery)
							            .build();
	    //API response obtained
	    SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
	    
	    if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
	    	return "";
	    }
	    
	    String ReturnData= ""; //Return String 
	    String Sitlinks= "";
	    //Iterate on all the obtained Sitelinks
	    for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	    	//Each campaign Can have multiple Sitelinks, Iterate on the sitelinks.
	    	for(int i=0; i<googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItemsCount(); i++)
	    	{
	    		//Sitelink_ids seperated by ; for each campaign id
	    		Sitlinks = Sitlinks + googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItems(i).split("/")[3]+ ";";
	    	}
	    }
	    
	    //Get the ids of sitelinks
	    String[] Sitelink_data= Sitlinks.split(";"); 
	    //Using each sitelink Id obtain its text and description as well as Final url
	   
	    for(int a=0; a<Sitelink_data.length; a++) {
	    
	    	String Feedid= Sitelink_data[a]; //Sitelink id
	    	//Campaign extension feed item resource name generated
	    	String campaignResourceName = ResourceNames.extensionFeedItem(Long.parseLong(customerId), Long.parseLong(Feedid));
	    	
	    	//Search Query for the sitelinks
	    	String searchQuery_Sitelinks = "SELECT extension_feed_item.resource_name, "
	    									+ "extension_feed_item.sitelink_feed_item.link_text, "
	    									+ "extension_feed_item.sitelink_feed_item.line1, "
	    									+ "extension_feed_item.sitelink_feed_item.line2, "
	    									+ "extension_feed_item.sitelink_feed_item.final_urls, "
	    									+ "extension_feed_item.status "
	    									+ "FROM extension_feed_item" 
	    									+ " WHERE extension_feed_item.extension_type = 'SITELINK'"; 
	    	 
	    	if (campaignId != null) {
	    		 searchQuery_Sitelinks += String.format(" AND extension_feed_item.resource_name  = '%s'",campaignResourceName);
	 	    }
	    	
	    	//Select Request with Page_size and customerId
	    	SearchGoogleAdsRequest request_sitelink = SearchGoogleAdsRequest.newBuilder()
							            				.setCustomerId((customerId))
							            				.setPageSize(PAGE_SIZE)
							            				.setQuery(searchQuery_Sitelinks)
							            				.build();
	    	//API response obtained
	    	SearchPagedResponse searchPagedResponse_sitelink = googleAdsServiceClient.search(request_sitelink);
	    	
	    	if (searchPagedResponse_sitelink.getPage().getResponse().getResultsCount() == 0) {
	    			System.out.println("Nothing");
	    			return "NOTHING";
	    	}
	    	//Iterate on the obtained sitelink ids data 
	    	for (GoogleAdsRow googleAdsRow : searchPagedResponse_sitelink.iterateAll()) {
	    			ExtensionFeedItem Feeditem_sitelink= googleAdsRow.getExtensionFeedItem(); //Extension Feed sitelink obtained
	    			ReturnData = ReturnData + campaignId + ";"; //Campaing id 
	    			ReturnData = ReturnData + Feeditem_sitelink.getSitelinkFeedItem().getLinkText() + ";"; 	//Sitelink_text
	    			ReturnData = ReturnData + Feeditem_sitelink.getSitelinkFeedItem().getLine1() + ";"; 	//Sitelink_line1
	    			ReturnData = ReturnData + Feeditem_sitelink.getSitelinkFeedItem().getLine2() + ";";		//Sitelink_line2
	    			ReturnData = ReturnData + Feeditem_sitelink.getSitelinkFeedItem().getFinalUrls(0) + ";";//Sitelink_FinalUrl
	    			ReturnData = ReturnData + Feeditem_sitelink.getStatus() + "~"; 							//Sitelink_status
	    	}
	    }
	    ReturnData =ReturnData.substring(0,ReturnData.length() - 1); //Removing the last ~ from the string 
	    return ReturnData;
	}

	// ============================== GET Callout FUNCTION ========================= //
	public static String getCallout(GoogleAdsClient googleAdsClient, String customerId, String campaignId) throws Throwable
	{	
			/**
			 * This function returns the list of Callout against a campaignId.
			 * 
			 * @param googleAdsClient the google ad client
			 * @param campaignId the campaign Id for which we want to extract the Callout Extensions
			 * @param customerId the client customer id
			 * 
			 * @returns a string with ; separated values and ~ separated records of Callout against a campaign Id. 
			 * 										"CampaignId;CallOut_Text;Status~
			 * 										 CampaignId;CallOut_Text;Status~"
			 * 										 ...."
			 */
			int PAGE_SIZE = 10000;
			
		    GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		    
		    //Select Query 
		    String searchQuery = "SELECT campaign_extension_setting.extension_feed_items "
		    						+ "FROM campaign_extension_setting "
		    						+ "WHERE campaign_extension_setting.extension_type = 'CALLOUT'";
		    
		    if (campaignId != null) {
		        searchQuery += String.format(" AND campaign.id = %s", campaignId);
		    }
		    
		    //Select Request with Page_size and customerId
		    SearchGoogleAdsRequest request =SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId((customerId))
								            .setPageSize(PAGE_SIZE)
								            .setQuery(searchQuery)
								            .build();
		    
		    //API response obtained
		    SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
		    
		    if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
		    	return "";
		    }
		    
		    String ReturnData= ""; //Return String 
		    String Callouts= "";
		    
		    //Iterate on all the obtained Callouts
		    for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
		    	//Each campaign Can have multiple Sitelinks, Iterate on the sitelinks.
		    	for(int i=0; i<googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItemsCount(); i++)
		    	{ 	
		    		//Sitelink_ids seperated by ; for each campaign id
		    		Callouts = Callouts + googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItems(i).split("/")[3]+ ";";
		    	}
		    }
		    
		    //Get the ids of Callout_id
		    String[] Callout_id= Callouts.split(";");
		    
		    //Using each Callout_id obtain its text and description 
		    for(int a=0; a<Callout_id.length; a++) {
		    
		    	String Feedid= Callout_id[a];
		    	//Extension Feed item is generated using the campaign id and call out ids 
		    	String campaignResourceName = ResourceNames.extensionFeedItem(Long.parseLong(customerId), Long.parseLong(Feedid));
		    	
		    	//Search Query generated for each ids to get its status and call out text 
		    	String searchQuery_Callouts = "SELECT extension_feed_item.callout_feed_item.callout_text, "
		    	 								+ "extension_feed_item.resource_name, "
		    	 								+ "extension_feed_item.status "
		    	 								+ "FROM extension_feed_item "
		    	 								+ "WHERE extension_feed_item.extension_type = 'CALLOUT'"; 
		    	
		    	if (campaignId != null) {
		    		 searchQuery_Callouts += String.format(" AND extension_feed_item.resource_name  = '%s'",campaignResourceName);
		 	    }
		    	
		    	//API request with the query 
		    	SearchGoogleAdsRequest request_callout =SearchGoogleAdsRequest.newBuilder()
								            			.setCustomerId((customerId))
								            			.setPageSize(PAGE_SIZE)
								            			.setQuery(searchQuery_Callouts)
								            			.build();
		    	//API response obtianed
		    	SearchPagedResponse searchPagedResponse_callout = googleAdsServiceClient.search(request_callout);
		    	if (searchPagedResponse_callout.getPage().getResponse().getResultsCount() == 0) {
		    			System.out.println("Nothing");
		    			return "NOTHING";
		    	}
		    	
		    	for (GoogleAdsRow googleAdsRow : searchPagedResponse_callout.iterateAll()) {
		    			ExtensionFeedItem Feeditem_CALLOUT= googleAdsRow.getExtensionFeedItem(); 
		    			ReturnData = ReturnData + campaignId + ";"; 												//CampaignID
		    			ReturnData = ReturnData + Feeditem_CALLOUT.getCalloutFeedItem().getCalloutText()+ ";";		//Callout text
		    			ReturnData = ReturnData + Feeditem_CALLOUT.getStatus() + "~";								//Status
		    	}
		    }
		    
		    ReturnData =ReturnData.substring(0,ReturnData.length() - 1); //Removing the ~ from the string end 
		    return ReturnData;
		}

	// ============================== GET STRUCTURED SNIPPETS FUNCTION ========================= //	
	public static String getstructured_snippet(GoogleAdsClient googleAdsClient, String customerId,String campaignId) throws Throwable
	{   
		/**
		 * This function returns the list of Structured Snippets against a campaignId.
		 * 
		 * @param googleAdsClient the google ad client
		 * @param campaignId the campaign Id for which we want to extract the structured snippets Extensions
		 * @param customerId the client customer id
		 * 
		 * @returns a string with ; separated values and ~ separated records of Structured snippets against a campaign Id. 
		 * 										"CampaignId;Header;StructuredSnippet_1;StructuredSnippet_1;Status~
		 * 										 CampaignId;Header;StructuredSnippet_1;StructuredSnippet_1;Status~
		 * 										 ...."
		 */
		int PAGE_SIZE = 10000;
		
	    GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
	    //Search Query for obtained the Structured Snippet feed item and ids is written
	    String searchQuery = "SELECT campaign_extension_setting.extension_feed_items "
	    						+ "FROM campaign_extension_setting "
	    						+ "WHERE campaign_extension_setting.extension_type = 'STRUCTURED_SNIPPET'";
	    
	    if (campaignId != null) {
	        searchQuery += String.format(" AND campaign.id = %s", campaignId);
	    }
	   //API requested with the Search Query 
	    SearchGoogleAdsRequest request =SearchGoogleAdsRequest.newBuilder()
							            .setCustomerId((customerId))
							            .setPageSize(PAGE_SIZE)
							            .setQuery(searchQuery)
							            .build();
	    //API response for the requested query is obtained 
	    SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
	    
	    if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
	    	return "";
	    }
	    
	    String ReturnData= ""; //Return Data String 
	    String S_snippets= "";
	    
	    //Obtained results are iterated and ids of structred snippets are obtained 
	    for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
	    	
	    	for(int i=0; i<googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItemsCount(); i++)
	    	{   //Structured snippets id is extracted from the resource name 
	    		S_snippets = S_snippets + googleAdsRow.getCampaignExtensionSetting().getExtensionFeedItems(i).split("/")[3]+ ";";
	    	}
	    }
	    
	    String[] S_snippet_ids= S_snippets.split(";"); //Strucuted snippets ids are separted 
	    //Iterated to obtained each id
	    for(int a=0; a<S_snippet_ids.length; a++) {
	    
	    	String Feedid= S_snippet_ids[a]; //Strucuted id 
	    	//Extension feed item resoource name is generated 
	    	String campaignResourceName = ResourceNames.extensionFeedItem(Long.parseLong(customerId), Long.parseLong(Feedid));
	    	//Query written to obtained snippets text and status using each id 
	    	String searchQuery_S_snippets = "SELECT extension_feed_item.structured_snippet_feed_item.header, "
	    	 								+ "extension_feed_item.structured_snippet_feed_item.values, "
	    	 								+ "extension_feed_item.status "
	    	 								+ "FROM extension_feed_item "
	    	 								+ "WHERE extension_feed_item.extension_type = 'STRUCTURED_SNIPPET'"; 
	    	if (campaignId != null) {
	    		 searchQuery_S_snippets += String.format(" AND extension_feed_item.resource_name  = '%s'",campaignResourceName);
	 	    }
	        //API is requested with the query 
	    	SearchGoogleAdsRequest request_S_snippets =SearchGoogleAdsRequest.newBuilder()
							            				.setCustomerId((customerId))
							            				.setPageSize(PAGE_SIZE)
							            				.setQuery(searchQuery_S_snippets)
							            				.build();
	    	//API response is obtained 
	    	SearchPagedResponse searchPagedResponse_S_snippets = googleAdsServiceClient.search(request_S_snippets);
	    
	    	if (searchPagedResponse_S_snippets.getPage().getResponse().getResultsCount() == 0) {
	    			System.out.println("Nothing");
	    			return "NOTHING";
	    	}
	    	//Iterate over the response obtained 
	    	for (GoogleAdsRow googleAdsRow : searchPagedResponse_S_snippets.iterateAll()) {
	    		ExtensionFeedItem Feeditem_S_snippets= googleAdsRow.getExtensionFeedItem();
	    		ReturnData = ReturnData + campaignId + ";"; 														//Campaign Id
	    		ReturnData = ReturnData + Feeditem_S_snippets.getStructuredSnippetFeedItem().getHeader()+ ";";		//Header
	    	
	    		for(int x=0; x<Feeditem_S_snippets.getStructuredSnippetFeedItem().getValuesCount(); x++) {
	    			ReturnData = ReturnData + Feeditem_S_snippets.getStructuredSnippetFeedItem().getValues(x)+ ";"; //Snippets
	    		}
	    	ReturnData = ReturnData + Feeditem_S_snippets.getStatus() + "~";										//Status
	    }
	   }
	    ReturnData =ReturnData.substring(0,ReturnData.length() - 1); //Removing the ~ from the end of the string 
	    
	    return ReturnData;
	}
	
	// ============================== SYNC campaign Status FUNCTION ========================= //	
	public static String synccampaign_status(GoogleAdsClient googleAdsClient, String customerId,String campaignId) throws Throwable
	{	
		/**
		 * This function allows us to sync the  campaignId status from front end 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of   campaignId
		 * 										"adw_campaignId~
		 * 										 adw_campaignId~
		 * 										 ...."
		 * A String is returned that return "adw_campaignId;Status~
		 * 									adw_campaignId;Status~
		 * 									adw_campaignId;Status~
		 * 										..."
		 */
		
		int PAGE_SIZE = 10000;
		String[] CampaingIds = campaignId.split("~");
		String ReturnData= "";
		
		int c=0;
		
		for( int a =0 ; a<CampaingIds.length; a++) {
			String CampName =ResourceNames.campaign(Long.parseLong(customerId),Long.parseLong(CampaingIds[a]));
			GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
			
			String searchQuery = "SELECT campaign.status, "
								+ "campaign.id "
								+ "FROM campaign ";
			
		    if (campaignId != null) {
			    	searchQuery += String.format(" WHERE campaign.resource_name= '%s' ",CampName);
			}
		  
		    SearchGoogleAdsRequest request_S_snippets =
			          SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId((customerId))
								            .setPageSize(PAGE_SIZE)
								            .setQuery(searchQuery)
								            .build();
		   
		    SearchPagedResponse searchPagedResponse_S_snippets = googleAdsServiceClient.search(request_S_snippets);
		   
		    if (searchPagedResponse_S_snippets.getPage().getResponse().getResultsCount() == 0) {
		    	return "NOTHING";
		    }
		   
		    for (GoogleAdsRow googleAdsRow : searchPagedResponse_S_snippets.iterateAll()) {
		    	Campaign CampOut = googleAdsRow.getCampaign();
		    	ReturnData = ReturnData + CampaingIds[c] + ";";
		    	ReturnData = ReturnData + CampOut.getStatus()+ "~";
		    	c++;
		    }
		   }
		    
		ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
		return ReturnData;
	}
	
	// ============================== SYNC AdGroup Status FUNCTION ========================= //
	public static String sync_adgroup_status_new(GoogleAdsClient googleAdsClient, String customerId,String AdgroupId) throws Throwable
	{   
		/**
		 * This function allows us to sync the AdGroups status from front end 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of adgroup id 
		 * 										"adw_adGroup_id~
		 * 										 adw_adGroup_id~
		 * 										 ...."
		 * A String is returned that return "Adw_adGroup_id;Status~
		 * 									Adw_adGroup_id;Status~
		 * 									Adw_adGroup_id;Status~ 
		 * 										..."
		 */
		
		int PAGE_SIZE = 10000;
		String[] Adgroupids = AdgroupId.split("~");
		String ReturnData= "";
		for( int a =0 ; a<Adgroupids.length; a++) {
			String AdGroupName =ResourceNames.adGroup(Long.parseLong(customerId),Long.parseLong( Adgroupids[a]));
	
			GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
			
			String searchQuery = "SELECT ad_group.id, "
					+ "ad_group.status "
					+ "FROM ad_group ";
			
		    if (Adgroupids[a] != null) {
			    	searchQuery += String.format(" WHERE ad_group.resource_name= '%s' ", AdGroupName);
			}

		    SearchGoogleAdsRequest request_S_snippets =
			          SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId((customerId))
								            .setPageSize(PAGE_SIZE)
								            .setQuery(searchQuery)
								            .build();
		   
		    SearchPagedResponse searchPagedResponse_S_snippets = googleAdsServiceClient.search(request_S_snippets);
		   
		   
		    for (GoogleAdsRow googleAdsRow : searchPagedResponse_S_snippets.iterateAll()) {
		    	AdGroup CampOut = googleAdsRow.getAdGroup();
		    	ReturnData = ReturnData + Adgroupids[a] + ";";
		    	if (CampOut.getStatus()==null) {
		    		ReturnData = ReturnData + ""+ "~";
			    }
		    	else {
		    	ReturnData = ReturnData + CampOut.getStatus()+ "~";
		    	}
		    }
		   }
		    
		ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
		return ReturnData;
	}
	
	// ============================== SYNC kEYWORD STATUS FUNCTION ========================= //
	public static String sync_keyword_status(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	 { 
		/**
		 * This function allows us to sync the keywords status from front end 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of keywords ids against an adgroup id 
		 * 										"adw_adGroup_id;adW_keyword_id,adW_keyword_id1,adW_keyword_id2,adW_keyword_id3,~
		 * 										 adw_adGroup_id;adW_keyword_id,adW_keyword_id1,adW_keyword_id2,adW_keyword_id3,~
		 * 										 ...."
		 * A String is returned that return "Adw_adGroup_id;adw_keyword_id;Status~
		 * 										Adw_adGroup_id;adw_keyword_id;Status~
		 * 										Adw_adGroup_id;adw_keyword_id;Status~ 
		 * 										..."
		 */
		
		int PAGE_SIZE = 10000;
		String[] adgroup_keyword = Data.split("~");
		
		String ReturnData= "";
		
		for(int i=0;i<adgroup_keyword.length;i++)
		{
			String[] adgroup_keyword_ids = adgroup_keyword[i].split(";");
			String AdGroupId= adgroup_keyword_ids[0];
			String KeywordId= adgroup_keyword_ids[1];
		    
			GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		
			String searchQuery ="SELECT ad_group.id, "
								+ "ad_group_criterion.criterion_id, "
								+ "ad_group_criterion.status "
								+ "FROM ad_group_criterion "
								+ "WHERE ad_group_criterion.type = 'KEYWORD' "
								+ "AND ad_group_criterion.negative = FALSE "
								+ " AND ad_group.id= " + AdGroupId
								+ " AND ad_group_criterion.criterion_id IN (" +  KeywordId + ")";
								
		    String next_page = "";
			
			do
			{
				SearchGoogleAdsRequest request_S_snippets = SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId((customerId))
								            .setPageSize(PAGE_SIZE)
								            .setPageToken(next_page)
								            .setQuery(searchQuery)
								            .build();
			
				SearchPagedResponse searchPagedResponse_S_snippets = googleAdsServiceClient.search(request_S_snippets);
		
				next_page = searchPagedResponse_S_snippets.getNextPageToken();
			   
				SearchPage searchPage = searchPagedResponse_S_snippets.getPage();
				
				for (GoogleAdsRow googleAdsRow : searchPage.getValues())
					{
						AdGroup CampOut = googleAdsRow.getAdGroup();
						AdGroupCriterion Keyinfo = googleAdsRow.getAdGroupCriterion();
						ReturnData = ReturnData + adgroup_keyword_ids[0] + ";"; //AdGroup id 
						ReturnData = ReturnData + Keyinfo.getCriterionId() + ";"; //Adw_keyword id
						if (CampOut.getStatus()==null) {
							ReturnData = ReturnData + ""+ "~";
						}
						else {
							ReturnData = ReturnData + Keyinfo.getStatus() + "~"; //Keyword Status
						}
					}
		   	}while(!next_page.equals(""));
		   
		}
		 
		ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
		return ReturnData;
	 }
	
	// ============================== SYNC kEYWORD LABEL FUNCTION ========================= //
	public static String sync_keyword_label(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	{	
		/**
		 * This function allows us to sync the keywords and associated labels to it from front end 
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param record a string with ; separated values and ~ separated records of keywords ids against an adgroup id 
		 * 										"adw_adGroup_id;adW_keyword_id,adW_keyword_id1,adW_keyword_id2,adW_keyword_id3,~
		 * 										 adw_adGroup_id;adW_keyword_id,adW_keyword_id1,adW_keyword_id2,adW_keyword_id3,~
		 * 										 ...."
		 * A String is returned that return "Adw_adGroup_id;adw_keyword_id;Status~
		 * 										Adw_adGroup_id;adw_keyword_id;Status~
		 * 										Adw_adGroup_id;adw_keyword_id;Status~ 
		 * 										..."
		 */
		int PAGE_SIZE = 10000;
		String[] adgroup_keywords = Data.split("~");
		String ReturnData= "";
		for(int i=0;i<adgroup_keywords.length;i++)
		{
			String[] adgroup_keywords_ids = adgroup_keywords[i].split(";");
			String AdGroupId= adgroup_keywords_ids[0];
			String KeywordId= adgroup_keywords_ids[1];
		    
			GoogleAdsServiceClient googleAdsServiceClient = googleAdsClient.getLatestVersion().createGoogleAdsServiceClient();
		
			String searchQuery ="SELECT ad_group.id, "
								+ "ad_group_criterion.criterion_id, "
								+ "ad_group_criterion.status, "
								+ "ad_group_criterion.labels "
								+ "FROM ad_group_criterion "
								+ "WHERE ad_group_criterion.type = 'KEYWORD' "
								+ "AND ad_group_criterion.negative = FALSE "
								+ " AND ad_group.id= " + AdGroupId
								+ " AND ad_group_criterion.criterion_id IN (" +  KeywordId + ")";
								
		    String next_page = "";
			
			do
			{
				SearchGoogleAdsRequest request_S_snippets = SearchGoogleAdsRequest.newBuilder()
								            .setCustomerId((customerId))
								            .setPageSize(PAGE_SIZE)
								            .setPageToken(next_page)
								            .setQuery(searchQuery)
								            .build();
			
				SearchPagedResponse searchPagedResponse_S_snippets = googleAdsServiceClient.search(request_S_snippets);
				next_page = searchPagedResponse_S_snippets.getNextPageToken();
			   
				SearchPage searchPage = searchPagedResponse_S_snippets.getPage();
				
				for (GoogleAdsRow googleAdsRow : searchPage.getValues())
					{
						AdGroupCriterion Keyinfo = googleAdsRow.getAdGroupCriterion();
						ReturnData = ReturnData + adgroup_keywords_ids[0] + ";"; //AdGroup id 
						ReturnData = ReturnData + Keyinfo.getCriterionId() + ";"; //Adw_keyword id
						for(int index=0; index< Keyinfo.getLabelsCount(); index++)
							{
								ReturnData = ReturnData + Keyinfo.getLabels(index).split("/")[3] + "*";
							}
						 ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
						 ReturnData = ReturnData + "~";
					}
		   	}while(!next_page.equals(""));
		   
		}
		
		ReturnData =ReturnData.substring(0,ReturnData.length() - 1);
		return ReturnData;
	}

	public static String change_account_name(GoogleAdsClient googleAdsClient, String customerId, String NewName) throws Throwable
	{	
		/**
		 * This function allows us to Change the Account name from the front End
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param NewName; Is the new desired Descriptive Name for the google Ads 
		 * 
		 */
		
		String resourceName = ResourceNames.customer(Long.parseLong(customerId));
		
		CustomerServiceClient client =googleAdsClient.getLatestVersion().createCustomerServiceClient();
	
        Customer Cus = Customer.newBuilder().setResourceName(resourceName).setDescriptiveName(NewName).build();
        CustomerOperation ai =CustomerOperation.newBuilder().setUpdate(Cus).setUpdateMask(FieldMasks.allSetFieldsOf(Cus)).build();
        MutateCustomerResponse CusMut = client.mutateCustomer(customerId, ai);
		
		return resourceName;
		
	}
	
	public static String edit_adgroup_status(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	{	
		/**
		 * This function allows us to Edit the AdGroup Status
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param Data; Is the new desired status for the google Ads and id of respective AdGroup
		 * 										"Adw_adGroup_id;Status~
		 * 										 Adw_adGroup_id;Status~
		 * 										 Adw_adGroup_id;Status~ 
		 * 										 ..."	
		 * 	
		 * Returns a String of AdGroup_ids that have changed status
		 * 										"Adw_adGroup_id~
		 * 										 Adw_adGroup_id~
		 * 										 Adw_adGroup_id~ 
		 * 										 ..."	
		 */
		
		String[] AdGroups = Data.split("~");
		List<AdGroupOperation> operations = new ArrayList<>();
		for( int i = 0 ; i<AdGroups.length; i++) 
		{
			String[] AdGroup_ids_status= AdGroups[i].split(";");
			String adGroupId= AdGroup_ids_status[0];
			String status= AdGroup_ids_status[1];
			System.out.println(status);
			String resourceName= ResourceNames.adGroup( Long.parseLong(customerId), Long.parseLong(adGroupId));
			if( status.contains("REMOVED")) {
				System.out.println(status);
				 AdGroupOperation adgo =AdGroupOperation.newBuilder().setRemove(resourceName).build();
				 operations.add(adgo);
			}
			else {
				AdGroup adg = AdGroup.newBuilder().setResourceName(resourceName).setStatus(AdGroupStatus.valueOf(status)).build();
				AdGroupOperation adgo = AdGroupOperation.newBuilder().setUpdate(adg).setUpdateMask(FieldMasks.allSetFieldsOf(adg)).build();
				operations.add(adgo);
			}		
		}
		AdGroupServiceClient AdgroupClient = googleAdsClient.getLatestVersion().createAdGroupServiceClient();
		MutateAdGroupsResponse adgr = AdgroupClient.mutateAdGroups(customerId, operations);
		return "TRUE" ;
	}
	
	public static String edit_campaign_status(GoogleAdsClient googleAdsClient, String customerId, String Data) throws Throwable
	{
		/**
		 * This function allows us to Edit the Campaign Status
		 * 
		 * @param googleAdsClient the google ad client
		 * @param customerId the client customer id
		 * @param Data; Is the new desired status for the google Ads and id of respective AdGroup
		 * 										"Adw_Campaign_id;Status~
		 * 										 Adw_Campaign_id;Status~
		 * 										 Adw_Campaign_id;Status~ 
		 * 										 ..."	
		 * 	
		 * Returns a String of AdGroup_ids that have changed status
		 * 										"Adw_Campaign_id~
		 * 										 Adw_Campaign_id~
		 * 										 Adw_Campaign_id~ 
		 * 										 ..."	
		 */
		String[] Campaigns = Data.split("~");
		List<CampaignOperation> operations = new ArrayList<>();
		for( int i = 0 ; i<Campaigns.length; i++) 
		{
			String[] Campaigns_ids_status=Campaigns[i].split(";");
			String Campaignid= Campaigns_ids_status[0];
			String status= Campaigns_ids_status[1];
			System.out.println(status);
			String resourceName= ResourceNames.campaign(Long.parseLong(customerId), Long.parseLong(Campaignid));
			if( status.contains("REMOVED")) {
				System.out.println(status);
				 CampaignOperation adgo =CampaignOperation.newBuilder().setRemove(resourceName).build();
				 operations.add(adgo);
			}
			else {
				Campaign adg = Campaign.newBuilder().setResourceName(resourceName).setStatus(CampaignStatus.valueOf(status)).build();
				CampaignOperation adgo = CampaignOperation.newBuilder().setUpdate(adg).setUpdateMask(FieldMasks.allSetFieldsOf(adg)).build();
				operations.add(adgo);
			}		
		}
		CampaignServiceClient CampaignClient = googleAdsClient.getLatestVersion().createCampaignServiceClient();
		MutateCampaignsResponse adgr = CampaignClient.mutateCampaigns(customerId, operations);
		return "TRUE" ;
	}
}