package com.lanfunoe.gocache.dto;

/**
 * 排行榜列表响应DTO
 */
public record RankListResponse(
    Long timestamp,
    Integer total,
    Integer showLine,
    Theme theme,
    java.util.List<RankInfo> info
) {
    public record Theme(
        java.util.List<?> classifyList,
        String bgImage,
        Font font
    ) {
        public record Font(
            String nt,
            String st,
            String line,
            String boldLine
        ) {}
    }

    public record RankInfo(
        java.util.List<?> children,
        String baseImg,
        String rankname,
        Integer newCycle,
        String banner9,
        String albumImg9,
        String tablePlaque,
        Integer updateFrequencyType,
        Long playTimes,
        String img9,
        Integer isCityRank,
        Integer classify,
        Integer haschildren,
        java.util.List<SongInfo> songinfo,
        Integer rankCid,
        String shareBg,
        Integer id,
        String jumpUrl,
        String albumCoverColor,
        String shareLogo,
        String bannerurl,
        String zone,
        Integer showPlayCount,
        Integer isvol,
        String rankIdPublishDate,
        Integer issue,
        String imgCover,
        Integer customType,
        String intro,
        Integer rankid,
        String updateFrequency,
        String banner7url,
        Integer showPlayButton,
        String videoEnding,
        String jumpTitle,
        Integer isTiming,
        Integer countDown,
        Extra extra,
        Integer ranktype,
        String imgurl
    ) {
        public record SongInfo(
            Long albumAudioId,
            TransParam transParam,
            String name,
            String author,
            String songname
        ) {
            public record TransParam(
                Integer payBlockTpl,
                String unionCover,
                String language,
                Long cpyAttr0,
                Integer musicpackAdvance,
                Integer display,
                Integer displayRate,
                java.util.Map<String, Object> qualitymap,
                Integer cpyLevel,
                Integer cpyGrade,
                Long cid,
                Long ogg128Filesize,
                java.util.Map<String, Long> classmap,
                String hashMultitrack,
                java.util.Map<String, Long> ipmap,
                String ogg320Hash,
                String ogg128Hash,
                Long ogg320Filesize
            ) {}
        }

        public record Extra(
            Resp resp
        ) {
            public record Resp(
                ScheduledReleaseConf scheduledReleaseConf,
                Integer fiveYearTotal,
                Integer newTotal,
                Integer enjoyTotal,
                Integer recentYearTotal,
                Integer followTotal,
                Integer allTotal,
                Integer vipTotal,
                java.util.List<RankTag> rankTag
            ) {
                public record ScheduledReleaseConf(
                    String scheduledReleaseTime,
                    Integer latestRankCid,
                    String latestRankCidPublishDate
                ) {}

                public record RankTag(
                    String desc,
                    Integer type
                ) {}
            }
        }
    }
}