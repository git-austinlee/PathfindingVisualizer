import java.util.ArrayList;

public final class MergeSort {

    private MergeSort() {}

    public static void merge(ArrayList<Node> list, int l, int m, int r) {
        int s1 = m - l + 1;
        int s2 = r - m;

        Node[] L = new Node[s1];
        Node[] R = new Node[s2];

        for( int i = 0; i < s1; ++i ) {
            L[i] = list.get(l+i);
        }
        for( int j = 0; j < s2; ++j ) {
            R[j] = list.get(m+1+j);
        }

        int i = 0, j = 0, k = l;

        while( i < s1 && j < s2 ) {
            if( L[i].getFCost() <= R[j].getFCost() ) {
                list.set(k, L[i]);
                i++;
            }
            else {
                list.set(k, R[j]);
                j++;
            }
            k++;
        }

        while( i < s1 ) {
            list.set(k, L[i]);
            i++;
            k++;
        }

        while( j < s2 ) {
            list.set(k, R[j]);
            j++;
            k++;
        }
    }

    public static void sort(ArrayList<Node> list, int l, int r) {
        if( l < r ) {
            int m = l + (r-l)/2;

            sort(list, l, m);
            sort(list, m+1, r);

            merge(list, l, m, r);
        }
    }

}
