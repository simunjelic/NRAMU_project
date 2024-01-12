package ba.sum.fsre.nramu_project.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ba.sum.fsre.nramu_project.fragment.AddPostFragment;
import ba.sum.fsre.nramu_project.fragment.PostsFragment;
import ba.sum.fsre.nramu_project.fragment.ProfileFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PostsFragment();
            case 1:
                return new AddPostFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
