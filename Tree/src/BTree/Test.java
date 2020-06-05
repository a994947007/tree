package BTree;

public class Test {
	public static void main(String[] args) {
		BTree<Integer> bTree = new BTree<Integer>();
		BTree.TreePrinter printer = bTree.new TreePrinter();
		for (int i = 1; i <= 20; i++) {
			bTree.add(i);
            System.out.println(printer.getString(bTree));  
		}
		
		for (int i = 1; i <= 20; i++) {
			bTree.remove(i);
            System.out.println(printer.getString(bTree));  
		}
	}
	
}

