package network.client;

import org.json.JSONArray;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;
import java.util.ArrayList;

public class JsonConvert {

    public static ArrayList<InfoAgent> ToListInfoAgent(JSONArray list){
        ArrayList<InfoAgent> listAgent = new ArrayList<InfoAgent>();
        for(int i = 0 ; i < list.length() ; ++i){
            InfoAgent agent = new InfoAgent(list.getJSONObject(i));
            listAgent.add(agent);
        }
        return listAgent;
    }

    public static boolean[][] ToListWalls(JSONArray list){
        boolean[][] matrice = new boolean[list.length()][list.get(0).toString().split(",").length];

        for (int j = 0 ; j < list.length() ; ++j) {

            String str = list.get(j).toString();
            str = str.replace("[","");
            str = str.replace("]", "");
            String[] tab = str.split(",");
            boolean[] tableau = new boolean[tab.length];

            for(int i = 0 ; i < tab.length ; ++i){
                matrice[j][i] = Boolean.parseBoolean(tab[i]);
            }
        }
        return matrice;
    }

    public static ArrayList<InfoItem> ToListInfoItem(JSONArray list){
        ArrayList<InfoItem> listItems = new ArrayList<InfoItem>();
        for(int i = 0 ; i < list.length() ; ++i){
            InfoItem item = new InfoItem(list.getJSONObject(i));
            listItems.add(item);
        }
        return listItems;
    }

    public static ArrayList<InfoBomb> ToListInfoBomb(JSONArray list){
        ArrayList<InfoBomb> listBomb = new ArrayList<InfoBomb>();
        for(int i = 0 ; i < list.length() ; ++i){
            InfoBomb item = new InfoBomb(list.getJSONObject(i));
            listBomb.add(item);
        }
        return listBomb;
    }
}
