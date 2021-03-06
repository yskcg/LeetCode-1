import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;




 

public class Answer {
	public static void main(String [] args)
	{
        Answer answer = new Answer();
        ArrayList<Integer> res = answer.findSubstring("barfoofoobarthefoobarman", new String[] {"bar","foo","the"});
        System.out.println(res);
	}
	
    public ArrayList<Integer> findSubstring(String s, String[] words) {
    	ArrayList<Integer> res = new ArrayList<Integer>();
    	if(words.length <=0)
    		return res;
    	
    	int wordLen = words[0].length();
    	HashSet<String> wordsSet = new HashSet<String>();
    	for(int i=0; i<words.length; i++){
    		if(!wordsSet.contains(words[i]))
    			wordsSet.add(words[i]);
    	}
    	
    	HashMap<String, Integer> exsited = new HashMap<String, Integer>();
    	int curRes= 0;
    	
    	for(int i=0; i<s.length()-wordLen+1;){
    		String curSub = s.substring(i, i+wordLen); 
    		if(wordsSet.contains(curSub)){
    			
    			if(!exsited.containsKey(curSub)){
        			if(exsited.size()==0)
        				curRes =i;
        			
        			exsited.put(curSub, i);
        			i+=wordLen;
        			
        			if(wordsSet.size() == exsited.size()){
        				res.add(curRes);
        				exsited.clear();
        			}    				
    			}
    			else{
    				//delete all smaller that exsited.get(curRes)
    				curRes = exsited.get(curRes)+wordLen;
    				exsited.put(curSub, i);
    			}
    			

    		}  
    		else{
    			i++;
    			exsited.clear();
    		}
    	}
    	return res;
    }	
	
	
    public int divide(int dividend, int divisor) {
        int remain = dividend;
        int res =0;
        
        if((dividend>0 && divisor>0) || (dividend<0 && divisor<0)){
            while(Math.abs(remain)>=Math.abs(divisor)){
                remain -= divisor;
                res++;
                if(res==Integer.MAX_VALUE)
                	break;
            }            
        }
        else{
            while(Math.abs(remain)>=Math.abs(divisor)){
                remain += divisor;
                res--;
            }                 
        }
        return res;
    }	

    public ListNode reverseKGroup(ListNode head, int k) {
    	if(head == null)
    		return head;
    	
    	ListNode next = head;
    	ListNode newHead = head; 	
    	ListNode lastGroup=null;
    	boolean assignHead = false;
    	
    	while(next != null){
    		boolean isThereK= true;
    		ListNode kth = next;
    		for(int i=1; i<k; i++){
    			if(kth.next == null){
    				isThereK = false;
    				next = kth.next;
    				break;
    			}
    			kth=kth.next;
    		}
    		
    		if(isThereK){
    			ListNode node2Re = next;
    			ListNode PreNode2Re = kth.next;

    			if(!assignHead){
    				newHead = kth;
    				assignHead = true;
    			}
    			else 
    				lastGroup.next = kth;
    			
    			lastGroup = node2Re;
    			for(int i=0; i<k; i++){
        			ListNode nextNode2Re = node2Re.next;
    				node2Re.next=PreNode2Re;
    				PreNode2Re = node2Re;
    				node2Re = nextNode2Re;
    			}
    			next = node2Re;
    		}
    	}
    	return newHead;
    }
    
    public class ListNode {
    	int val;
        ListNode next;
        ListNode(int x) { val = x; }
     }    
	
    public int strStr(String haystack, String needle) {
    	int needleLen = needle.length();
    	
        for(int i=0; i<haystack.length()-needleLen; i++){
        	int temp=i;      	
        	for(int j=0; j<needleLen; j++, temp++){
        		if(haystack.charAt(temp)!=needle.charAt(j))
        			break;
        		 
        		if(j== needleLen-1)
        			return i;  		
        	}
        }
        return 0;
    }
	
	public class QueueItem {
	    public QueueItem(QueueItem item) {
			this.val=item.val;
			this.x = item.x;
			this.y = item.y;
		}
		public QueueItem() {
			// TODO Auto-generated constructor stub
		}
		public String val;
	    public int x;//num of (	
	    public int y;//num of )
	    
	}	
	
    public ArrayList<String> generateParenthesis(int n) {
    	Queue<QueueItem> q = new LinkedList<QueueItem>();
    	QueueItem start = new QueueItem();
    	start.val = "(";
    	start.x=1;
    	start.y=0;
    	
    	q.add(start);
    	
    	for(int i =1; i<2*n; i++ ){
    		while(!q.isEmpty() && q.peek().val.length()==i){
    			QueueItem item = q.remove();
    			if(item.x<n){
    				QueueItem Newitem = new QueueItem(item);
    				Newitem.val = Newitem.val+"(";
    				Newitem.x++;
    				q.add(Newitem);
    				//item.x++;
    			}
    			if(item.x-item.y>0 && item.y<n){
    				QueueItem Newitem = new QueueItem(item);
    				Newitem.val = Newitem.val+")";
    				Newitem.y++;
    				q.add(Newitem);
    			}
    		}
    	}
    	
    	ArrayList<String> res = new ArrayList<String>();
    	for(QueueItem item: q){
    		res.add(item.val);
    	}
    	return res;
    }	
}
