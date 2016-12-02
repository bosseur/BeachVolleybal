package nl.bosseur.beachvolleybal.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.activity.WorldTourActivity;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;

/**
 * Created by bosseur on 23/06/15.
 */
public class WorldTourAdapter extends BaseAdapter{

    private List<BeachTournament> events;
    private Activity activity;

    public WorldTourAdapter(WorldTourActivity worldTourActivity, List<BeachTournament> events) {
        this.events = events;
        this.activity = worldTourActivity;
    }

    @Override
    public int getCount() {
        return this.events.size();
    }

    @Override
    public BeachTournament getItem(int position) {
        return this.events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holder;
        if( convertView != null){
            holder = (ViewHoler) convertView.getTag();
        }else{
            int layout = position % 2 == 0 ? R.layout.world_tour_event_even : R.layout.world_tour_event_uneven;
            convertView = this.activity.getLayoutInflater().inflate(layout, parent, false);
            holder = new ViewHoler(convertView);
            convertView.setTag( holder );

        }
        BeachTournament event = this.events.get(position);
        holder.eventName.setText(event.getTile());

        DateFormat df = new SimpleDateFormat(activity.getString(R.string.date_format));

        holder.startDate.setText( df.format( event.getStartDate() ));
        holder.endDate.setText( df.format( event.getEndDate() ));

        return convertView;
    }

    private class ViewHoler {
        TextView eventName;
        TextView startDate;
        TextView endDate;
        public ViewHoler(View view) {
            eventName = (TextView) view.findViewById(R.id.event_name);
            startDate = (TextView) view.findViewById(R.id.start_date);
            endDate = (TextView) view.findViewById(R.id.end_date);
        }
    }
}
