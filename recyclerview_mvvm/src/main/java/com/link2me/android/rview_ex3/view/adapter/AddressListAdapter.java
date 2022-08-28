package com.link2me.android.rview_ex3.view.adapter;

import static com.link2me.android.rview_ex3.view.activity.AddressActivity.constraintLayout;
import static com.link2me.android.rview_ex3.view.activity.AddressActivity.isCheckFlag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.link2me.android.rview_ex3.R;
import com.link2me.android.rview_ex3.databinding.ItemAddressBinding;
import com.link2me.android.rview_ex3.model.Address_Item;
import com.link2me.android.rview_ex3.network.RetrofitURL;

/***
 * https://www.youtube.com/watch?v=xPPMygGxiEo 동영상 시청하면 개념 이해하는데 엄청 도움됨
 */

// RecyclerView.Adapter 대신에 ListAdapter<T, VH> 를 상속한다.
// T는 리사이클러뷰가 화면에 표시할 데이터의 타입이다.
public class AddressListAdapter extends ListAdapter<Address_Item, AddressListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    Context context;

    // 인터페이스 선언 -------------------------------------------------------------
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClicked(Address_Item item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    // 인터페이스 ----------------------------------------------------------------

    public AddressListAdapter(Context context, @NonNull DiffUtil.ItemCallback<Address_Item> diffCallback) {
        super(diffCallback);
        this.context = context;
    }

    public void selectAll(int list_count){ // checkbox 전체 선택
        for(int i=0;i < list_count;i++){
            getItem(i).setCheckBoxState(true);
        }
        notifyDataSetChanged();
    }
    public void unselectall(int list_count){ // checkbox 전체 해제
        for(int i=0;i < list_count;i++){
            getItem(i).setCheckBoxState(false);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 실제로 데이터를 표시하는 부분
        //Address_Item currentItem = rvItemList.get(position);
        Address_Item currentItem = getItem(position);
        holder.bindItem(currentItem, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemAddressBinding itemBinding;

        public ViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bindItem(Address_Item item, int position){
            String photoURL = RetrofitURL.BaseUrl + "photos/" + item.getPhoto();
            if(photoURL.contains("null")){
                Glide.with(context).asBitmap().load(R.drawable.photo_base).into(itemBinding.profileImage);
            } else {
                Glide.with(context).asBitmap().load(photoURL).into(itemBinding.profileImage);
            }
            itemBinding.childName.setText(item.getUserNM());
            itemBinding.childMobileNO.setText(PhoneNumberUtils.formatNumber(item.getMobileNO()));
            itemBinding.childOfficeNO.setText(PhoneNumberUtils.formatNumber(item.getTelNO()));

            itemBinding.profileImage.setOnClickListener(view1 -> {
                if(mListener != null){
                    mListener.onItemClicked(item,position); // 클릭의 결과로 값을 전달
                }
            });

            final String[] items ={"휴대폰 전화걸기","사무실전화 걸기"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("해당작업을 선택하세요");
            builder.setItems(items, (dialog, which) -> {
                Toast.makeText(context, items[which] + "선택했습니다.", Toast.LENGTH_SHORT).show();
                switch (which){
                    case 0:
                        if(item.getMobileNO().length() ==0){
                            Toast.makeText(context, "전화걸 휴대폰 번호가 없습니다.",Toast.LENGTH_SHORT).show();
                            break;
                        }

                        AlertDialog dialog1 = new AlertDialog.Builder(context)
                                .setTitle(item.getUserNM())
                                .setMessage(PhoneNumberUtils.formatNumber(item.getMobileNO()) + " 통화하시겠습니까?")
                                .setPositiveButton("예",
                                        (dialog23, which13) -> {

                                            Intent intent = new Intent(Intent.ACTION_CALL,
                                                    Uri.parse("tel:" + PhoneNumberUtils.formatNumber(item.getMobileNO())));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        })
                                .setNegativeButton(
                                        "아니오",
                                        (dialog22, which12) -> dialog22.dismiss()).create();
                        dialog1.show();
                        break;
                    case 1:
                        if(item.getTelNO().length() ==0){
                            Toast.makeText(context, "전화걸 사무실 번호가 없습니다.",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        AlertDialog dialog2 = new AlertDialog.Builder(context)
                                .setTitle(item.getUserNM())
                                .setMessage(PhoneNumberUtils.formatNumber(item.getTelNO()) + " 통화하시겠습니까?")
                                .setPositiveButton("예",
                                        (dialog3, which1) -> {

                                            Intent intent = new Intent(Intent.ACTION_CALL,
                                                    Uri.parse("tel:" + PhoneNumberUtils.formatNumber(item.getTelNO())));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        })
                                .setNegativeButton(
                                        "아니오",
                                        (dialog32, which14) -> dialog32.dismiss()).create();
                        dialog2.show();
                        break;

                }
            });
            builder.create();

            itemBinding.callBtn.setOnClickListener(v -> builder.show());

            if (isCheckFlag == false) {
                itemBinding.callBtn.setVisibility(View.VISIBLE);
                itemBinding.callBtn.setOnClickListener(view -> builder.show());
                itemBinding.listcellCheckbox.setVisibility(View.GONE);
                itemBinding.childLayout.setOnClickListener(view ->
                        Toast.makeText(context, "상세보기를 눌렀습니다 ===" + position + " , idx :" + item.getIdx(), Toast.LENGTH_SHORT).show());

                itemBinding.childLayout.setOnLongClickListener(v -> {
                    isCheckFlag = true;
                    constraintLayout.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    return true;
                });
            } else {
                itemBinding.callBtn.setVisibility(View.GONE);
                //convertView.setClickable(false);
                itemBinding.listcellCheckbox.setVisibility(View.VISIBLE);
                itemBinding.listcellCheckbox.setTag(position); // This line is important.

                // 체크 박스 클릭하면 CheckBoxState 에 반영한다. setOnCheckedChangeListener 대신 사용
                itemBinding.listcellCheckbox.setOnClickListener(v -> {
                    if(getItem(position).getCheckBoxState() == true){
                        getItem(position).setCheckBoxState(false);
                        Log.d("checkbox","position : "+ position + " checkBoxState === "+ getItem(position).getCheckBoxState());
                    } else {
                        getItem(position).setCheckBoxState(true);
                        Log.d("checkbox","position : "+ position + " checkBoxState === "+ getItem(position).getCheckBoxState());
                    }
                });

                itemBinding.childLayout.setOnClickListener(v -> {
                    if(itemBinding.listcellCheckbox.isChecked() == false){
                        itemBinding.listcellCheckbox.setChecked(true);
                        getItem(position).setCheckBoxState(true);
                        //notifyDataSetChanged();
                        Log.d("checklist","position : "+ position + " checkBoxState === "+ getItem(position).getCheckBoxState());
                    } else {
                        itemBinding.listcellCheckbox.setChecked(false);
                        getItem(position).setCheckBoxState(false);
                        //notifyDataSetChanged();
                        Log.d("checklist","position : "+ position + " checkBoxState === "+ getItem(position).getCheckBoxState());
                    }
                });
            }

            // 재사용 문제 해결
            if(getItem(position).getCheckBoxState() == true){
                itemBinding.listcellCheckbox.setChecked(true);
                Log.d("ReUse","position : " + position + " checkBoxState === " + getItem(position).getCheckBoxState());
            } else {
                itemBinding.listcellCheckbox.setChecked(false);
                Log.d("ReUse","position : "+position + " checkBoxState ===" + getItem(position).getCheckBoxState());
            }
        }
    }

}
