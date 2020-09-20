package com.atlas.gateway;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AtlasGatewayAdapter extends RecyclerView.Adapter<AtlasGatewayAdapter.GatewayViewHolder> {

    private List<AtlasGateway> gatewayList;

    public AtlasGatewayAdapter(List<AtlasGateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    @NonNull
    @Override
    public AtlasGatewayAdapter.GatewayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gateway_item, parent, false);
        return new GatewayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtlasGatewayAdapter.GatewayViewHolder holder, final int position) {
        final AtlasGateway atlasGateway = gatewayList.get(position);

        holder.bind(atlasGateway);

        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = atlasGateway.isExpanded();
                atlasGateway.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gatewayList.size();
    }

    public static class GatewayViewHolder extends RecyclerView.ViewHolder {

        public View avatar;
        public TextView alias;
        public TextView identity;
        public TextView psk;
        public TextView keepAliveCounter;
        public TextView lastRegisteredTime;
        public TextView lastKeepAlive;
        public ImageButton showMore;
        public LinearLayout container;

        public GatewayViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            alias = itemView.findViewById(R.id.alias);
            identity = itemView.findViewById(R.id.identity);
            psk = itemView.findViewById(R.id.psk);
            keepAliveCounter = itemView.findViewById(R.id.keepAliveCounter);
            lastRegisteredTime = itemView.findViewById(R.id.lastRegisteredTime);
            lastKeepAlive = itemView.findViewById(R.id.lastKeepAlive);
            container = itemView.findViewById(R.id.container);
            showMore = itemView.findViewById(R.id.expandImageButton);
        }

        private void bind(AtlasGateway gateway) {
            GradientDrawable drawable = (GradientDrawable) avatar.getBackground();

            /* set  the color of circle */
            if (gateway.isRegistered())
                drawable.setColor(Color.parseColor("#00FF00"));
            else
                drawable.setColor(Color.parseColor("#808080"));

            alias.setText(gateway.getAlias());
            identity.setText(gateway.getIdentity());
            psk.setText(gateway.getPsk());
            keepAliveCounter.setText(Integer.toString(gateway.getKeepAliveCounter()));
            lastRegisteredTime.setText(gateway.getLastRegisteredTime());
            lastKeepAlive.setText(gateway.getLastKeepAlive());

            /* show container */
            if (gateway.isExpanded()) {
                container.setVisibility(View.VISIBLE);
                showMore.setRotation(180);
            } else {
                container.setVisibility(View.GONE);
                showMore.setRotation(0);
            }
        }
    }
}
