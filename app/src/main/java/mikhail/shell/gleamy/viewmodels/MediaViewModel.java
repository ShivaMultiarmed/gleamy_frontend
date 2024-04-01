package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.repositories.MediaRepository;

public class MediaViewModel extends ViewModel {
    private final MediaRepository mediaRepository;
    private final MutableLiveData<ActionModel> mediaData;
    private final Queue<ActionModel> pendingMedia;
    private final Long userid;
    public MediaViewModel(Long userid)
    {
        this.userid = userid;
        mediaRepository = MediaRepository.getInstance(GleamyApp.getApp());
        mediaData = new MutableLiveData<>();
        pendingMedia = new LinkedList<>();
    }
    public void fetchMediaPortion(Long portion_num, LifecycleOwner subscriber, Observer<List<Media>> observer)
    {
        MutableLiveData<List<Media>> mediaListData = new MutableLiveData<>();
        mediaRepository.fetchMediaPortionByUserId(userid, portion_num, mediaListData);
        mediaListData.observe(subscriber, observer);
    }
    public void fetchMediaById(String mediaid, LifecycleOwner subscriber, Observer<byte[]> observer)
    {
        MutableLiveData<byte[]> mediaData = new MutableLiveData<>();
        mediaRepository.fetchMediaById(mediaid, mediaData);
        mediaData.observe(subscriber, observer);
    }
    public static class Factory implements ViewModelProvider.Factory
    {
        private final Long userid;
        public Factory(Long userid)
        {
            this.userid = userid;
        }
        @Override
        public <T extends ViewModel> T create(Class<T> type) {
            return (T) new MediaViewModel(userid);
        }
    }
}
