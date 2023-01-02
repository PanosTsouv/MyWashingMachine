package com.example.mywashingmachine;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class WashingProgram  implements Parcelable {
    String titleName;
    int spins;
    int temp;
    int duration;
    String description;
    int selectedImage = View.INVISIBLE;
    boolean hasPreWash;
    boolean hasSqueezing;
    boolean isDefaultProgram;

    public WashingProgram(String titleName, int spins, int temp, int duration, String description,
                          boolean hasPreWash, boolean hasSqueezing, boolean isDefaultProgram)
    {
        this.titleName = titleName;
        this.spins = spins;
        this.temp = temp;
        this.duration = duration;
        this.description = description;
        this.hasPreWash = hasPreWash;
        this.hasSqueezing = hasSqueezing;
        this.isDefaultProgram = isDefaultProgram;
    }

    protected WashingProgram(Parcel in) {
        titleName = in.readString();
        spins = in.readInt();
        temp = in.readInt();
        duration = in.readInt();
        description = in.readString();
        selectedImage = in.readInt();
        hasPreWash = in.readByte() != 0;
        hasSqueezing = in.readByte() != 0;
        isDefaultProgram = in.readByte() != 0;
    }

    public static final Creator<WashingProgram> CREATOR = new Creator<WashingProgram>() {
        @Override
        public WashingProgram createFromParcel(Parcel in) {
            return new WashingProgram(in);
        }

        @Override
        public WashingProgram[] newArray(int size) {
            return new WashingProgram[size];
        }
    };

    public String getTitleName()
    {
        return this.titleName;
    }

    public void setTitleName(String titleName){ this.titleName = titleName; }

    public int getSelectedImage()
    {
        return this.selectedImage;
    }

    public void setSelectedImage(int selectedImage){ this.selectedImage = selectedImage; }

    public int getSpins()
    {
        return this.spins;
    }
    public void setSpins(int spins) {this.spins = spins;}

    public int getTemp()
    {
        return this.temp;
    }
    public void setTemp(int temp) {this.temp = temp;}

    public int getDuration()
    {
        return this.duration;
    }
    public void setDuration(int duration) {this.duration = duration;}

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description) {this.description = description;}

    public boolean getHasPreWash()
    {
        return this.hasPreWash;
    }

    public void setHasPreWash(boolean hasPreWash){this.hasPreWash = hasPreWash;}

    public boolean getHasSqeezing()
    {
        return this.hasSqueezing;
    }

    public void setHasSqeezing(boolean hasSqueezing){this.hasSqueezing = hasSqueezing;}

    public boolean getIsDefaultProgram()
    {
        return this.isDefaultProgram;
    }

    public void setIsDefaultProgram(boolean isDefaultProgram){this.isDefaultProgram = isDefaultProgram;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleName);
        dest.writeInt(spins);
        dest.writeInt(temp);
        dest.writeInt(duration);
        dest.writeString(description);
        dest.writeInt(selectedImage);
        dest.writeByte((byte) (hasPreWash ? 1 : 0));
        dest.writeByte((byte) (hasSqueezing ? 1 : 0));
        dest.writeByte((byte) (isDefaultProgram ? 1 : 0));
    }
}
