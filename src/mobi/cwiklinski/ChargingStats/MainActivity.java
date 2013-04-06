package mobi.cwiklinski.ChargingStats;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import mobi.cwiklinski.ChargingStats.adapter.ChargeAdapter;
import mobi.cwiklinski.ChargingStats.model.Charge;


public class MainActivity extends ListActivity
{

    private Charge chargeModel = new Charge();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(
            new ChargeAdapter(
                this,
                getCursor()
            )
        );
        registerForContextMenu(getListView());
    }

    private Cursor getCursor() {
        Charge model = new Charge();
        return model.getAll();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ChargeAdapter.ChargeViewHolder holder =
            (ChargeAdapter.ChargeViewHolder) ((AdapterView.AdapterContextMenuInfo) menuInfo).targetView.getTag();
        menu.setHeaderTitle(R.string.action);
        menu.add(
            1,
            3,
            ContextMenu.NONE,
            getResources().getString(R.string.delete)
        );
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo   = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ChargeAdapter.ChargeViewHolder holder = (ChargeAdapter.ChargeViewHolder) menuInfo.targetView.getTag();
        switch(item.getItemId()) {
            case 3:
                chargeModel.delete(holder.id);
                ((CursorAdapter) getListAdapter()).swapCursor(getCursor());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
