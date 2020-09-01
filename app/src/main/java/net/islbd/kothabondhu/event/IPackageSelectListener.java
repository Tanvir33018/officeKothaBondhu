package net.islbd.kothabondhu.event;

public interface IPackageSelectListener {
    void onPackageSelection(String callId);

    void onPackageSelection(String callId, String imageUrl);
}
