package com.taidii.diibot.net;

import android.os.Parcel;
import android.os.Parcelable;

import com.taidii.diibot.utils.JsonUtils;
import com.taidii.diibot.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public final class NetworkBean {
	public static class PostToEditAvatarRsp {
		public int status;
		public String avatar;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class CommbookUploadImageRsp {
		public String avatar;
		public String content;
		public String content_type;
		public long create_by;
		public String create_time;
		public long id;
		public String image_url;
		public String name;
		public long student;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class PostToGetTokenAndProfileRsp {
		public String token;
		public UserWithChild user;
		public int relation_type;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class UserWithChild {
		public String username;
		public String fullname;
		public long pk;
		public String avatar;
		public String email;
		public String user_type;
		public Child[] children;
		public Guardian guardian;
		public String[] app_permissions;
		public Center center;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Guardian {
		public long id;
	}

	public static class Center {
		public long id;
	}

	public static class Child {
		public long id;
		public String fullname;
		public int birth_range;
		@SerializedName("nickname")
		public String nickName;
		public String avatar;
		public byte gender;
		public String date_of_birth;
		public School[] centers;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class School {
		public int id;
		public String name;
		public String logo;
		public String ngportfolio;
		public MiscConfig miscConfig;
		public String center_type;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class MiscConfig {
		public boolean canReply;
		public boolean canShare;
		public boolean purchased;
		public boolean allow_parent_attendance;
	}

	public static class CommentDetail {
		public String comment_date;
		public String comment_text;
		public int from;
		public long comment_by_id;
		public String comment_by_name;
		public long id;
		public String comment_date_humanize_en;
		public String comment_date_humanize_zh;
	}

	public static class PostToSendMomentCommentRsp {
		public int status;
		public CommentDetail comment;
	}

	public static class CommBookMessage implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		public long id;
		public int student;
		public String content;
		public String image_url;
		public String avatar;
		public String name;
		public int create_by;
		public String create_time;
		public String content_type;
		public String audio_length;
		public String filename;
		public boolean recalled;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class GetCommBookRsp {
		public String next;
		public String previous;
		public CommBookMessage[] results;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class CombookUnreadCount {
		public long student;
		public int count;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class PostToGetQiniuTokenRsp {
		public String uptoken;
		public String qiniu_key;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class MomentComment implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		public int id;
		public String comment_date;
		public String comment_text;
		public int comment_from;
		public int comment_by_id;
		public String comment_by_name;
		public String avatar;
		public String humanize_en;
		public String humanize_zh;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Moment {
		public String back_date;
		public String owner_name;
		public String avatar;
		public String approved_date;
		public int batch_id;
		public String publish_at;
		public MomentPhoto[] uploaded_photos;
		public String comments_and_observations;
		public long timestamp;
		public int comments_count;
		public byte is_praised;
		public int praise_count;
		public String publish_at_humanize_en;
		public String publish_at_humanize_zh;
		public int expected_no_of_photo;
		public int version;
		public int shared;
		public String batch_number;
		public int validated_by_parent;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class MomentPhoto implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public String thumb;
		public String photo;
		public String photo_url;
		public String comments;
		public String caption;
		public String location;
		public String takendate;
		public int id;
		public String upload_date;
		public CenterTag center_tag;
		public AssessmentSubject assessment_subject;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class CenterTag implements Serializable {
		private static final long serialVersionUID = 1L;
		public String name_en;
		public String name_other;
		public int id;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class AssessmentSubject implements Serializable {
		private static final long serialVersionUID = 1L;
		public String name;
		public int id;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}


	public static class GetMomentRsp {
		public int status;
		public Moment[] photos;
		public byte canReply;
		public byte canShare;
		public byte allowDownload;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class GetMomentSortedByDateRsp {
		public String next;
		public MomentPhoto[] results;
		public String previous;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class GetPortfolioRsp {
		public int status;
		public PortfolioDetail[] portfolios;
		public int page;
		public int[] pages;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class PortfolioDetail {
		public String[] portfolio;
		public String term;
		public int year;
		public long id;
		public String last_update;
		public String center_name;
		public long timestamp;
		public String author_url;
		public String author_name;
		public String content;
		public String pdf;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Records {
		public long id;
		public String pick_up;
		public String attendance_type;
		public String temperature;
		public String weight;
		public String recorded_on;
		public String imageurl;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Overviews {
		public Records[] records;
		public String date;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class GetAttendanceRsp {
		public int status;
		public Overviews[] overviews;
		public String date;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Happening {
		public String[] images;
		public String published_by_url;
		public String published_by_name;
		public String send_date;
		public int id;
		public String title;
		public String center_name;
		public long timestamp;
		public boolean isread;
		public String pdf;

	}

	public static class GetHappeningsRsp {
		public int status;
		public Happening[] results;
		public int totals;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class EditNameRsp {
		public int status;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class EditTelRsp {
		public int status;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class EditEmailRsp {
		public int status;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class EditAddressRsp {
		public int status;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class EditPasswordRsp {
		public int status;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class UserInfo {
		public long id;
		public String name;
		public String avatar_thumbnail_150x150;
		public String contact_no;
		public String address;
		public String email;
	}

	public static class WeeklyUpdate {
		public long id;
		public String week;
		public String title;
		public String submit_by_url;
		public String submit_by_name;
		public String[] image_urls;
		public String share_date;
		public long timestamp;
		public String pdf;

	}

	public static class GetWeeklyUpdateRsp {
		public int status;
		public WeeklyUpdate[] results;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Attachment {
		public String url;
		public String name;
	}

	public static class Announcement {
		public long id;
		public String title;
		public String published_by_url;
		public long timestamp;
		public String content;
		public String tag;
		public boolean isread;
		public String send_date;
		public String published_by_name;
		@SerializedName("attachements")
		public Attachment[] attachments;
		public String pdf;

	}

	public static class GetAnnouncementRsp {
		public int status;
		public Announcement[] results;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class GetMenuRsp {
		public int status;
		public Menu[] menus;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class Menu {
		public long centerid;
		public String[] menu;

	}

	public static class PostToPraiseRsp {
		public int status;
		public long photobatch_id;
		public byte is_praised;
		public int praise_count;

		@Override
		public String toString() {
			return JsonUtils.toJson(this);
		}
	}

	public static class UploadMediaRsp {
		public String uptoken;
		public String qiniu_key;
	}

	public static class AddChild {
		public long id;
		public String avatar;
		@SerializedName("date_of_birth")
		public String dateOfBirth;
		@SerializedName("enter_date")
		public String enterDate;
		@SerializedName("fullname")
		public String fullName;
		@SerializedName("nickname")
		public String nickMame;
		public int gender;
	}

	public static class EditChild {
		public String avatar;
		@SerializedName("date_of_birth")
		public String dateOfBirth;
		@SerializedName("enter_date")
		public String enterDate;
		@SerializedName("fullname")
		public String fullName;
		@SerializedName("nickname")
		public String nickMame;
		public int gender;
	}

	public static class UpdatePhotoRsp implements Parcelable {
		public String story;
		@SerializedName("created_date")
		public String createdDate;
		@SerializedName("childrens")
		public List<String> children;
		public List<String> tags;

		public UpdatePhotoRsp() {
		}

		protected UpdatePhotoRsp(Parcel in) {
			story = in.readString();
			createdDate = in.readString();
			children = in.createStringArrayList();
			tags = in.createStringArrayList();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(story);
			dest.writeString(createdDate);
			dest.writeStringList(children);
			dest.writeStringList(tags);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Creator<UpdatePhotoRsp> CREATOR = new Creator<UpdatePhotoRsp>() {
			@Override
			public UpdatePhotoRsp createFromParcel(Parcel in) {
				return new UpdatePhotoRsp(in);
			}

			@Override
			public UpdatePhotoRsp[] newArray(int size) {
				return new UpdatePhotoRsp[size];
			}
		};
	}

	public static class PostToGetQiNiuRsp {
		public String uptoken;
		public String qiniu_key;

		@Override
		public String toString() {
			return Utils.logObject(this);
		}
	}
}
