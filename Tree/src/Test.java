import RedBlackTree.RBTree;
import binaryTree.Visitor;


public class Test {
	public static void main(String[] args) {
		int arr[] = {1,2,3,4,5,6};
		RBTree<Integer> tree = new RBTree<Integer>();
		for (int i : arr) {
			tree.add(i);
		}
		
		tree.preTraver(new Visitor<Integer>() {
			@Override
			public boolean visit(Integer e) {
				System.out.println(e);
				return false;
			}
		});

		System.out.println("-------------------------");
		tree.remove(5);
		tree.preTraver(new Visitor<Integer>() {
			@Override
			public boolean visit(Integer e) {
				System.out.println(e);
				return false;
			}
		});
		System.out.println("-------------------------");	
		tree.remove(6);
		tree.preTraver(new Visitor<Integer>() {
			@Override
			public boolean visit(Integer e) {
				System.out.println(e);
				return false;
			}
		});
		System.out.println("-------------------------");	
		tree.inTraver(new Visitor<Integer>() {
			@Override
			public boolean visit(Integer e) {
				System.out.println(e);
				return false;
			}
		});
	}
	
}

