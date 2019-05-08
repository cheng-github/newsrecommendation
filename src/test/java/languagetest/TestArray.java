package languagetest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class TestArray {

    @Test
    public void testContains() {
        UserPair.addUserPair(1, 2);
        UserPair.addUserPair(2, 3);
        UserPair.addUserPair(1, 2);
        System.out.println(UserPair.userPairs);
    }

    @Test
    public void testArray() {
        System.out.println(Math.abs(-1.1));
    }

    @Test
    public void testHash() {
        HashMap<String, String> map = new HashMap<>();
        map.put("111", "111");
        setValue(map);
        System.out.println(map);
    }

    private void setValue(HashMap<String, String> map){
        System.out.println(map);
        map.put("222", "222");
    }

    @Test
    public void testsplit() {
        String test = "　　一份湖南省槟榔食品行业协会下发的《关于停止广告宣传的通知》（下文简称《通知》）在网上广为传播。该文件提到，湖南所有槟榔生产企业即日起停止国内全部广告宣传，且此项工作必须在3月15日前全部完成。　　3月7日晚，澎湃新闻（www.thepaper.cn）从湖南省槟榔食品行业协会秘书长处证实，该通知系协会3月7日所发，“应湖南省市场监督管理局的要求。”　　湖南省槟榔食品行业协会发布的《关于停止广告宣传的通知》（来源：长沙当地人士）　　澎湃新闻获取的该份《通知》写到：为落实湖南省市场监督管理局对（槟榔）行业相关企业的行政指导会议及常务理事会特别会议精神，所有企业即日起停止国内全部广告宣传，停止发布的媒介平台包含且不限于报纸、电台、电视台、高速公路、机场、铁路列车、地铁、公交车、网络平台、电子屏、店招、影院、出租车顶等。　　《通知》要求，此项工作必须在3月15日前全部完成。各企业务必顾全大局、加强组织、按时落实。各企业间要互相监督，对未按要求落实相关工作任务的企业，市场管理部门将取证并采取相应措施。　　值得一提的是，伴随着致癌争议，湖南槟榔已由小作坊发展成一个有着百亿产值的行业。随着槟榔行业发展壮大，各类槟榔品牌广告开始出现在媒体平台。　　据新华社2月26日报道，国家卫生委办公厅印发的《健康口腔行动方案（2019-2025年）》中提到，国家食品药品监督管理总局在2017年公布致癌物清单时，已将槟榔果列入一级致癌物。北京大学口腔医院口腔黏膜科教授、中华口腔医学会副秘书长刘宏伟表示，我国南方一些省份的群众有咀嚼槟榔的习惯，从而导致了口腔癌的发病率增高。　　上述报道提到，在国内咀嚼槟榔盛行的地区如海南省、湖南省等地，绝大多数的口腔癌是由咀嚼槟榔导致的。2018年中华口腔医学会和中国疾控中心曾调研湖南省群众嚼槟榔和口腔癌的现状，当时中南大学湘雅医院口腔颌面外科病房内，50位住院患者有45人患口腔癌，其中44人有长期、大量咀嚼槟榔病史。　　对于此次禁止槟榔企业发布广告的通知是否与槟榔有害口腔健康有关，湖南省槟榔食品行业协会秘书长未予回应，但其表示，广告被全部禁止后，或给湖南槟榔行业销售带来重大打击。";
        String[] result = test.split("　　");
        System.out.println(result);
    }

    @Test
    public void testAddall() {
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        ArrayList<Integer> list3 = new ArrayList<>();
        // 用于测试remove
        ArrayList<Integer> list4 = new ArrayList<>();
        list4.add(3);
        list4.add(1);
        list4.add(6);
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list3.add(5);
        list3.add(6);
        list3.add(7);
        list1.addAll(list2);
        list1.addAll(list3);

        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }
        list1.removeAll(list4);
        System.out.println("------------");
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }
    }

    @Test
    public void testSetAddAll() {
        Set<Integer> set1 = new HashSet();
        Set<Integer> set2 = new HashSet();
        Set<Integer> set3 = new HashSet();
        Set<Integer> set4 = new HashSet();
        set1.add(1);
        set1.add(2);
        set1.add(3);
        set2.add(1);
        set2.add(2);
        set2.add(3);

    }
}
