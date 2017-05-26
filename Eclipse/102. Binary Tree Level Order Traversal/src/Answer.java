import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Answer {
    public class TreeNode {
    	int val;
    	TreeNode left;
    	TreeNode right;
    	TreeNode(int x) { val = x; }
    }
    
	public static void main(String [] args)
	{
        Answer answer = new Answer();
        TreeNode root = answer.new TreeNode(2);
        TreeNode left = answer.new TreeNode(1);
        TreeNode right = answer.new TreeNode(3);
        root.left=left;
        root.right= right;
        List<List<Integer>> res = answer.levelOrder_dfs(root);
        System.out.println(res);       
	}
	
	//We can also do DFS
	public List<List<Integer>> levelOrder_dfs(TreeNode root) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        
        levelHelper(res, root, 0);
        return res;
    }
	
	private void levelHelper(List<List<Integer>> res, TreeNode root, int height){
		if(root == null)
			return;
		
		if(height>=res.size())
			res.add(new ArrayList<Integer>());
		
		res.get(height).add(root.val);
		levelHelper(res,root.left,height+1);
		levelHelper(res,root.right,height+1);
	}
	
	//bfs
    public List<List<Integer>> levelOrder(TreeNode root) {
    	List<List<Integer>> res = new ArrayList<List<Integer>>();
    	if(root == null)
    		return res;
    	
    	Queue<TreeNode> q = new LinkedList<TreeNode>();			
    	q.add(root);
    	
    	while(!q.isEmpty()){
    		Queue<TreeNode> tempQ = new LinkedList<TreeNode>();
    		List<Integer> oneLevel = new ArrayList<Integer>();
        	while(!q.isEmpty()){	
        		TreeNode cur = q.poll();
    			oneLevel.add(cur.val);
    			
    			if(cur.left != null)
    				tempQ.add(cur.left);
    			if(cur.right != null)
    				tempQ.add(cur.right);
        	}
        	q=tempQ;
        	res.add(oneLevel);
    	}
    	return res;
    }
    
    public List<List<Integer>> levelOrder2(TreeNode root) {
    	List<List<Integer>> res = new LinkedList<List<Integer>>();
        if(root == null) 
        	return res;
        
        Queue<TreeNode> q = new LinkedList<TreeNode>();      
        q.offer(root);
        while(!q.isEmpty()){
            int levelNum = q.size();
            List<Integer> oneLevel = new LinkedList<Integer>();
            for(int i=0; i<levelNum; i++) {
            	TreeNode cur = q.poll();
            	
                if(cur.left != null) 
                	q.offer(cur.left);
                if(cur.right != null) 
                	q.offer(cur.right);
                
                oneLevel.add(cur.val);
            }
            res.add(oneLevel);
        }
        return res;
    }
}
