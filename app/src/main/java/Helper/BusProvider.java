package Helper;



import com.squareup.otto.Bus;




/*
*
 * Created by Muhammad Shan on 02/04/2016.
 */



public final class BusProvider {

        private static final Bus BUS = new Bus();

        public static Bus getInstance() {
            return BUS;
        }

        private BusProvider() {
            // No instances.
        }

}


