package tin;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.auth.oauth2.UserCredentials;

public class Test {
	public static void main(String[] args) throws Throwable {
		
		String refreshToken = "1//03eQ2BNvUDuqwCgYIARAAGAMSNwF-L9Ir0yo30TxR0n_k-T9tzM-xNJtjq39_0-ww7g_HOhIqzLPYAkB7seZAY3X3C6D3hIiHTY0";
		String clientSecret = "jCjlsniZ5PYa-bkFKYJxnF1g";
		String clientId = "545698149904-j5se2v1qskq65r2ubt806i1346rctpd2.apps.googleusercontent.com";
		String developerToken = "2oyP4SIhLpELVEyboY5B4g";
		Long clientCustomerLoginId = 4017552764L;
		
		GoogleAdsClient googleAdsClient = Master.setCredentials(refreshToken, clientId, clientSecret, clientCustomerLoginId, developerToken);
		
		Long clientCustomerId = 3100077637L;
		Long adGroupId = 120559339813L;
		
		//System.out.println(Master.createAccount(googleAdsClient, "TESTING00", "USD", "America/New_York", 4017552764L));
		
		//Master.Negativelocationfunction(googleAdsClient,"3100077637", "12749868649", "21137");
		
//		Master.GetCampaigns(googleAdsClient, clientCustomerId);
		
		
		//================================ Get Ad Groups ============================================== //
		
//		Long campaign_id = 12749868649L;		
//		String s = Master.getadgroup(googleAdsClient,  campaign_id, clientCustomerId);
//		System.out.println("-----------\n"+"Ad Group details: "+s);
//		ad_group containing eta 120559339813
		
		//================================ Get Expanded Text Ads ===================================== //
//		
//		System.out.println(Master.geteta(googleAdsClient, clientCustomerId, 120559339813L));
//		
		//================================ Add New Ad Groups ========================================= //
		/*
		String str = "12749868649"+";TEST1235"+";ENABLED"+";1;OPTIMIZE"+";34";  
		String str2 = "12749868649"+";TEST1234"+";ENABLED"+";1;OPTIMIZE"+";56";  
		String in = str + "~" + str2;			
		System.out.println(Master.addAdGroup(googleAdsClient, clientCustomerId, in));
		*/
		//================================ Add New Expanded Text Ads ================================ //
		/*
		String data = "120559339813;headline1text;headline2text;headline3text;description1text;description2text;path1text;path2text;"
				+ "http://search.movingthemitten.com/search?s[locations][0][city]=SterlingHeights&s[state]=MI&s[propertyTypes][]="
				+ "condo&pvl=1&lvl=1&hvl=1&s[photoCountMin]=1&utm_source=google-adwords&r=1&noEdit=1&rType=SPLIT_B;ENABLED;123";
		String add_eta_response = Master.addExpandedTextAds(googleAdsClient, clientCustomerId, data1); 
		
		System.out.println(add_eta_response);
		*/
		//================================ Add New Responsive Search Ads ============================ //
		/*
		String data = "120559339813;headline1text;headline2text;headline3text;headline4text;hea234dline2text;h34eadline2text;h12eadline2text;head52line2text;head124line2text;headli34ne2text;headlin1324e2text;headl234ine2text;headl345ine2text;he12345adline2text;hea456fdline2text;descdfgription1text;descfdgription2text;desfdgcri54ption1text;desfdgcrip876tion1text;path1text;path2text;http://search.movingthemitten.com/search?s[locations][0][city]=SterlingHeights&s[state]=MI&s[propertyTypes][]=condo&pvl=1&lvl=1&hvl=1&s[photoCountMin]=1&utm_source=google-adwords&r=1&noEdit=1&rType=SPLIT_B;ENABLED;123;";		
		System.out.println(Master.addResponsiveSearchAds(googleAdsClient, clientCustomerId, data));
		*/ 
		/*
		String data = "120559339813;10727961;PAUSED;0";
		System.out.println(Master.updateKeywordStatus(googleAdsClient, clientCustomerId, data));
		*/
//		
//		System.out.println(Master.getrsa(googleAdsClient, clientCustomerId, adGroupId));
		
		/*
		String x = "120559339813;1234;Cherry Hill Village Condominiums for sale;https://search.mrwestside.com/search?s[locations][0][neighborhood]=Mid-City&s[city]=Mid-City&s[state]=CA&s[propertyTypes][]=condo&pvl=1&lvl=1&hvl=1&s[photoCountMin]=1&utm_source=google-adwords&r=1&noEdit=1&limit=8&rType=SPLIT_B;EXACT;ENABLED";
		System.out.println(Master.addKeywords(googleAdsClient, clientCustomerId, x));
		*/
//		String input = "120559339813~530554638690";  //adGroupId~ETA Id
//		System.out.println(Master.syncETAStatus(googleAdsClient, clientCustomerId, input));
//		Master.geteta(googleAdsClient, clientCustomerId, 120559339813L);
		
//		Master.getSitelink(googleAdsClient, "3100077637", "12749868649");
		
//		System.out.println(Master.syncRSAStatus(googleAdsClient, clientCustomerId, "120559339813~514370022462"));
		
		
		
		// Master.GetCampaigns(googleAdsClient, clientCustomerId);
		String[] weekDays = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
		//String[] hourMinutes = {"0","0","16","45"};
		String[] hourMinutes = {"0","0","15","0","15","0","24","0"};
		String campname = "Testing Campaign Hammad 13/7/21";
		//String[] hourMinutes = {"0","0","15","0","15","0","24","0"};
		//Master.addCampaign(googleAdsClient, clientCustomerId, null, null, null, false, false, false, false, null, null, null, null, null, null, null, null, null, null, null);
		
		long campaign_id = Master.addCampaign(googleAdsClient, clientCustomerId,"PRESENCE_OR_INTEREST","UNSPECIFIED","SEARCH",true,true,false,false,"",true,"0","Manual CPC","abdc",campname,"","20","Standard","PAUSED",weekDays,hourMinutes,"30");
		System.out.println("Campaign ID: " + campaign_id);

//		Master.updateLocation(googleAdsClient,Long.toString(clientCustomerId),"13694232629", "1019657");
//		Master.Negetivelocationfunction(googleAdsClient,Long.toString(clientCustomerId),"12749868649", "21137");
//		Master.updateLanguage(googleAdsClient,Long.toString(clientCustomerId),"13694232629", "1000");
	
		
		
		//======================= Upload Sitelink test code ======================================//
		
		//System.out.println(Master.sitelinks(googleAdsClient,Long.toString(clientCustomerId),"Find Homes & Condos Here;Special Home Financing Programs;Explore Neighborhoods & Contact Us;https://search.jstalley.com/search?s[locations][0][county]=Miami-Dade%20County&s[locations][0][state]=FL&s[propertyTypes][0]=house&s[propertyTypes][1]=condo&pvl=1&lvl=1&hvl=1&photoCountMin=1&utm_source=google-adwords&r=1&noEdit=1&limit=8&rType=SPLIT_B;ENABLED;5","13666635725"));
		
		//======================= Upload Callout test code ======================================//
		
		//System.out.println(Master.Callout(googleAdsClient,Long.toString(clientCustomerId),"13666635725","B1;ENABLED~B2;ENABLED~B3;ENABLED~B4;ENABLED~B5;ENABLED~B6;ENABLED~"));
		
		//======================= Upload Structured Snippet test code ======================================//
		
	    // System.out.println(Master.StructuredSnippets(googleAdsClient,Long.toString(clientCustomerId), "13694232629","Brands;aaaaa aaaa, bbbbbbbb bbbbbb, asdasda asdasdsd;ENABLED~Courses ;111aaaaa aaaaaa, bbbbbb bbbbb, asdkasdn asdnasd;ENABLED");
		
		//======================= Upload Create Label test code ======================================//
		
	  //  System.out.println(Master.create_labels(googleAdsClient,Long.toString(clientCustomerId),"112~114~113"));
		
		//System.out.println("OutPut: " + id);
		
		//String str = "12749868649"+";Ham1"+";ENABLED"+";1;OPTIMIZE"+";34";
//		String str2 = "12749868649"+";Ham2"+";ENABLED"+";1;OPTIMIZE"+";56";  
//		String in = str + "~" + str2;			
//		System.out.println(Master.addAdGroup(googleAdsClient, clientCustomerId, in));
//		String id = Master.add_keyword_labels(googleAdsClient,Long.toString(clientCustomerId),"120559339813;14379467441;18809748405;1234~120559339813;14379467441;18809748408;1234");
//		String x = "115743989959;1074387;condominiums for sale bel aire south bay view;https://search.seevirginiabeachvahomes.com/v2/search?s[locations][0][neighborhood]=Bel+Aire/South+Bay+View&s[locations][0][city]=Norfolk&s[locations][0][state]=VA&s[propertyTypes][]=Condo&pvl=1&lvl=1&hvl=1&photoCountMin=1&utm_source=google-adwords&r=1&noEdit=1&limit=8&rType=SPLIT_B;EXACT;PAUSED";
//	
		//======================= Get Location test code ======================================//
		
		//System.out.println(Master.getlocation(googleAdsClient, Long.toString(clientCustomerId),"13694232629"));
		
		//======================= Get Negative Location test code ======================================//
		
		//System.out.println(Master.getneglocation(googleAdsClient, Long.toString(clientCustomerId),"13694232629"));
		
		//======================= Get Negative Keyword test code ======================================//
		
		//System.out.println(Master.getnegkeyword(googleAdsClient, Long.toString(clientCustomerId),"13694232629"));
		
		//======================= Get Sitelink test code ======================================//
		
		//System.out.println(Master.getSitelink(googleAdsClient, Long.toString(clientCustomerId), "12749868649"));
		
		//======================= Get Callout test code ======================================//
		
//		System.out.println(Master.getCallout(googleAdsClient, Long.toString(clientCustomerId), "13694232629"));
		
		//======================= Get Structured Snippets test code ===========================//
		
		//System.out.println(Master.getstructured_snippet(googleAdsClient, Long.toString(clientCustomerId), "13694232629"));
		
		// ============================== sync Campaign Status ========================= //
		//System.out.println(Master.synccampaign_status(googleAdsClient, Long.toString(clientCustomerId), "13694232629~12749868649"));

		// ============================== sync AdGroup Status ========================= //
		//System.out.println(Master.sync_adgroup_status_new(googleAdsClient, Long.toString(clientCustomerId), "120559339813~122897314263~119440027370~119655299450~119655299490"));

		// ============================== sync Keyword Status ========================= //
		//System.out.println(Master.sync_keyword_status(googleAdsClient, Long.toString(clientCustomerId), "120559339813;10727961,10733151,11351333,13788736"));
		
		// ============================== sync Keyword Label ========================= //
		//System.out.println(Master.sync_keyword_label(googleAdsClient, Long.toString(clientCustomerId), "120559339813;13203309135,14379467441"));
		//Master.change_account_name(googleAdsClient,"4084414717", "NewName_checking");
		
		//=================================================================================//
		//==================Edit_adgroup_status====================//
		//System.out.println(Master.edit_adgroup_status(googleAdsClient, Long.toString(clientCustomerId) ,"120559339813;ENABLED~"));
		
		//==================Edit_campaign_status====================//
		//System.out.println(Master.edit_campaign_status(googleAdsClient, Long.toString(clientCustomerId) ,"12749868649;REMOVED~"));
				
	}	
	private static void print(String str) {
		System.out.println(str);
	}
	
}