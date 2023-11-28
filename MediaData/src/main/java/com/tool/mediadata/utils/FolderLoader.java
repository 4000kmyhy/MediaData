package com.tool.mediadata.utils;

import android.content.Context;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tool.mediadata.entity.Folder;
import com.tool.mediadata.entity.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 14:55
 **/
public class FolderLoader {

    /**
     * 通过歌曲列表获取文件夹列表
     *
     * @param musics //按路径排序的歌曲列表
     */
    private static List<Folder> getFolderList(List<Music> musics, String name) {
        List<Folder> folderList = new ArrayList<>();
        Map<String, Folder> folderMap = new LinkedHashMap<>();
        try {
            for (Music music : musics) {
                File file = new File(music.getData()).getParentFile();
                if (file != null) {
                    String folderName = file.getName();
                    String path = file.getPath();

                    Folder folder = folderMap.get(path);
                    if (folder == null) {
                        folder = new Folder(folderName, path, 1);
                        folderMap.put(path, folder);
                    } else {
                        folder.setMusicCount(folder.getMusicCount() + 1);
                        folderMap.put(path, folder);
                    }
                }
            }
            Set<String> set = folderMap.keySet();
            for (String path : set) {
                Folder folder = folderMap.get(path);
                if (folder != null) {
                    if (TextUtils.isEmpty(name)) {
                        folderList.add(folder);
                    } else {
                        if (folder.getName().toLowerCase().contains(name.toLowerCase())) {
                            folderList.add(folder);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folderList;
    }

    public static List<Folder> getFolderList(Context context, String name) {
        List<Music> musics = MusicLoader.getMusicList(context, null, MediaStore.Audio.Media.DATA, null);
        return getFolderList(musics, name);
    }

    public static List<Folder> getFolderList(Context context) {
        return getFolderList(context, null);
    }
}
