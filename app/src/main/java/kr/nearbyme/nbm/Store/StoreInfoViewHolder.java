package kr.nearbyme.nbm.Store;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.nearbyme.nbm.R;
import kr.nearbyme.nbm.data.Shop;

/**
 * Created by CHOIMOONYOUNG on 2016. 5. 20..
 */
public class StoreInfoViewHolder extends RecyclerView.ViewHolder{
    TextView scoreView, storeNameView, storeInfoView, storeAdressView, storeHourView, storeCallView,
            storePriceNameView, storePriceView, storeDesignerView, storeDesignerNameView, storeDesignerInfoView, styleNameView;

    ImageView storeImageView, designerImageView;
    RatingBar ratingBar;
    Button btnCall, btnLike, btnMap;

    Shop mData;

    public StoreInfoViewHolder(View itemView) {
        super(itemView);
        storeImageView = (ImageView) itemView.findViewById(R.id.imageStore);
        btnCall = (Button) itemView.findViewById(R.id.btn_call);
        btnLike = (Button) itemView.findViewById(R.id.btn_like);
        btnMap = (Button) itemView.findViewById(R.id.btn_map);
        storeNameView = (TextView) itemView.findViewById(R.id.text_storename);
        storeInfoView = (TextView) itemView.findViewById(R.id.text_storeInfo);
        storeAdressView = (TextView) itemView.findViewById(R.id.text_storeAddress);
        storeHourView = (TextView) itemView.findViewById(R.id.text_storeHour);
        storeCallView = (TextView) itemView.findViewById(R.id.text_storeNum);
        storePriceNameView = (TextView) itemView.findViewById(R.id.text_price);
        storePriceView = (TextView) itemView.findViewById(R.id.text_priceReceive);
        storeDesignerView = (TextView) itemView.findViewById(R.id.textDesigner);
        designerImageView = (ImageView) itemView.findViewById(R.id.image_designerIcon);
        storeDesignerNameView = (TextView) itemView.findViewById(R.id.text_designerName);
        storeDesignerInfoView = (TextView) itemView.findViewById(R.id.text_designerInfo);
        styleNameView = (TextView) itemView.findViewById(R.id.text_style);
        scoreView = (TextView) itemView.findViewById(R.id.text_score);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_store);




    }

    public void setStoreInfo(Shop data){
        mData = data;

        if(data!=null) {

            Glide.with(storeImageView.getContext()).load(data.getShop_pic()).into(storeImageView);
            storeInfoView.setText(data.getShop_name());
            storeAdressView.setText(data.getShop_address());
            storeHourView.setText(data.getShop_time());
            storeCallView.setText(data.getShop_phone());
            storePriceView.setText(data.getShop_price());
            scoreView.setText(Double.toString(data.getShop_score()));

//            Glide.with(designerImageView.getContext()).load(data.dsnr_info.get(0).getDsnr_profilePic()
//            ).into(designerImageView);
//            storeDesignerNameView.setText(data.dsnr_info.get(0).getDsnr_name());
//            storeDesignerInfoView.setText(data.dsnr_info.get(0).getDsnr_info());
//
//
//            for (int i = 1; i < data.dsnr_info.size(); i++) {
//                Glide.with(designerImageView.getContext()).load(data.dsnr_info.get(i).getDsnr_profilePic()
//                ).into(designerImageView);
//                storeDesignerNameView.append(data.dsnr_info.get(i).getDsnr_name());
//                storeDesignerInfoView.append(data.dsnr_info.get(i).getDsnr_info());
//                System.out.println();
//            }

        }

    }
}

