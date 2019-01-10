<template>
	
	<div class="uploader-single-file" :class="{'uploader-show-screens' : showScreens}" :data-compact="compact"
	     :data-upload-state="dataUploadState">
		
		
		
		<vue-file-selector @file-selected="fileDescriptor => fileSelected(fileDescriptor)"
		                   @error="(errorMessageKey) => fileSelectorError(errorMessageKey)"
		                   :allow-directory-selection="false"
		                   v-show="!readonly"></vue-file-selector>
		<div class="uploader-progress-bar uploader-progress-bar-show" v-if="showScreens && screen === 'progress'"
		     :style="{width: uploadPercent}"></div>
		<div class="uploader-screens">
			<div class="uploader-screen uploader-screen-details" v-if="showScreens && screen === 'details'"
			     :class="{active: screen === 'details'}">
				<slot name="details"></slot>
			</div>
			<div class="uploader-screen uploader-screen-error" v-if="showScreens && screen === 'error'"
			     :class="{active: screen === 'error'}">
				<ul>
					<li>
						<span class="label">{{$t('plugin.uploader.label.fileName')}}</span>
						<span class="value">{{fileName}}</span>
					</li>
					<li>
						<span class="label">{{$t('plugin.uploader.label.fileSize')}}</span>
						<span class="value">{{fileSize | byte}}</span>
					</li>
					<li>
						<span class="label">{{$t('plugin.uploader.label.error')}}</span>
						<span class="value" :class="uploadState.result.type === 'serverError' ? 'error-message' : ''">{{$t('plugin.uploader.result.' + uploadState.result.type)}}
						<span v-if="uploadState.customStatusMessage && uploadState.result.type === 'serverError'">({{$t('plugin.uploader.customStatusMessage.' + uploadState.customStatusMessage.key, uploadState.customStatusMessage.parameters)}})</span>
					</span>
					</li>
				</ul>
				<button type="button" class="btn danger" @click="reset()">{{$t('plugin.uploader.button.ackerror')}}</button>
			</div>
			<div class="uploader-screen uploader-screen-progress" v-if="showScreens && screen === 'progress'"
			     :class="{active: screen === 'progress'}">
				<ul>
					<li>
						<span class="label">{{$t('plugin.uploader.label.state')}}</span>
						<span class="value">{{$t('plugin.uploader.state.' + uploadState.state)}}
						<span v-if="uploadState.customStatusMessage">({{$t('plugin.uploader.customStatusMessage.' + uploadState.customStatusMessage.key, uploadState.customStatusMessage.parameters)}})</span>
					</span>
					</li>
					<li>
						<span class="label">{{$t('plugin.uploader.label.fileName')}}</span>
						<span class="value">{{fileName}}</span>
					</li>
					<li>
						<span class="label">{{$t('plugin.uploader.label.progress')}}</span>
						<span class="value">{{uploadState.progress | percent}}</span>
					</li>
					<li>
						<span class="label">{{$t('plugin.uploader.label.fileSize')}}</span>
						<span class="value">{{fileSize | byte}}</span>
					</li>
				</ul>
				<button type="button" class="btn danger" @click="cancelUpload()">
					{{$t('plugin.uploader.button.cancelUpload')}}
				</button>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
	import { IFileDescriptor, IUpload, IUploadState, ModernUpload } from '@ponte/file-upload-js';
	import VueFileSelector from "@/components/VueFileSelector.vue";
	import { Component, Prop, Vue } from 'vue-property-decorator';
	
	export const FILE_SELECTED_EVENT = 'file-selected';
	export const ERROR_EVENT = 'error';
	export const FINISHED = 'finished';
	
	@Component({
		components: {
			VueFileSelector,
		}
	})
	export default class VueSingleUpload extends Vue {
		
		@Prop() private showDetails: boolean | undefined;
		@Prop() private readonly: boolean | undefined = false;
		@Prop() private compact: boolean | undefined;
		@Prop() private baseUrl: string | undefined;
		
		private screen: 'details' | 'progress' | 'error' = 'progress';
		private showScreens: boolean = false;
		private upload: IUpload | undefined;
		private uploadState: IUploadState | null = null;
		private fileName = '';
		private fileSize = 0;
		
		
		public mounted() {
			this.reset();
		}
		
		public destroyed() {
			this.cancelUpload();
		}
		
		public async uploadFile(file: File, userInfo: any): Promise<any> {
			this.fileName = file.name;
			this.fileSize = file.size;
			this.upload = new ModernUpload({
				file: file,
				userInfo: userInfo,
				uploadIdURL: this.baseUrl + '/getid',
				uploadURL: this.baseUrl + '/upload',
				progressQueryURL: this.baseUrl + '/getstatus',
				headers: {
					'X-Requested-With': 'IqApp',
				},
			});
			this.uploadState = this.upload.uploadState;
			this.upload.addUploadStateChangeListener(() => {
				if (this.upload !== undefined) {
					this.uploadState = this.upload.uploadState;
				}
			});
			this.screen = 'progress';
			this.showScreens = true;
			return this.upload.startUpload()
					.then((result) => {
						this.screen = 'details';
						if (result.name != null) {
							this.fileName = result.name;
						}
						this.$emit(FINISHED);
						
						return result;
					}).catch((err) => {
						this.screen = 'error';
						throw err;
					});
		}
		
		public reset() {
			this.showScreens = false;
			this.uploadState = null;
		}
		
		public cancelUpload() {
			if (this.upload) {
				return this.upload.abortUpload();
			}
			return undefined;
		}
		
		public showDetailsScreen() {
			this.screen = 'details';
			this.showScreens = true;
			this.uploadState = null;
		}
		
		get uploadPercent() {
			if (!this.uploadState || !this.uploadState.progress) {
				return '0%';
			}
			return (Math.round(this.uploadState!.progress * 10000) / 100) + '%';
		}
		
		protected fileSelected(fileDescriptor: IFileDescriptor) {
			this.$emit(FILE_SELECTED_EVENT, fileDescriptor);
		}
		
		public fileSelectorError(errorMessageKey: string) {
			this.$emit(ERROR_EVENT, errorMessageKey);
		}
		
		public configure(baseUrl: string, showDetails: boolean, readonly: boolean, compact: boolean) {
			this.baseUrl = baseUrl;
			this.showDetails = showDetails;
			this.readonly = readonly;
			this.compact = compact;
		}
		
		private get dataUploadState() {
			return this.uploadState !== null ? this.uploadState.state : 'default';
		}
		
	}
	
	export interface UploadResult {
		fileId: string;
		mineType: string;
		name: string;
		total: number;
	}
	
	

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  lang="scss">
	
	//FIXME
	$light-border-gray: #dedede;
	$trd-green: #68CEA2;
	$sec-dark-gray: #555;
	$light-blue: #378caf;
	$pri-green: #21A96F;
	$blue: #276b9f;
	$pri-gray: #cccccc;
	$grapefruit-hover: #DA4453;
	
	.uploader-single-file {
		$iconWidth: 70px;
		$iconHeight: 40px;
		$thumbnailSize: 100px;
		$padding: 10px;
		min-height: $thumbnailSize + $padding * 2;
		border: 1px solid $light-border-gray;
		border-radius: 2px;
		position: relative;
		z-index: 0;
		.uploader-dropzone {
			overflow: hidden;
			transition: 0.3s ease visibility, 0.3s ease opacity, 0.3s ease transform;
			.disable-animations & {
				transition-duration: 0s;
			}
			position: absolute;
			width: 100%;
			height: 100%;
			left: 0;
			top: 0;
			z-index: 3;
			text-align: center;
			&::before {
				content: "";
				display: block;
				height: 50%;
				padding-bottom: $iconHeight / 2;
			}
			.uploader-dropzone-label {
				margin: 0 10px 10px 10px;
			}
			.file-selector {
				cursor: pointer;
				position: absolute;
				width: 100%;
				height: 100%;
				top: 0;
				left: 0;
				&.file-selector-drag-over {
					background-color: rgba($trd-green, 0.5);
				}
				form {
					cursor: pointer;
					position: absolute;
					z-index: 1;
					opacity: 0;
					display: none;
					input {
						cursor: pointer;
					}
				}
			}
		}
		//.uploader-icon {
		//  position: absolute;
		//  left: 50%;
		//  top: 50%;
		//  margin-left: -$iconWidth/2;
		//  margin-top: -$iconHeight/2;
		//  height: $iconHeight;
		//  transition: 0.3s ease margin-left, 0.3s ease left, 0.3s ease opacity;
		//  .disable-animations & {
		//    transition-duration: 0s;
		//  }
		//  &::before {
		//    display: block;
		//    line-height: $iconHeight;
		//    background-repeat: no-repeat;
		//    background-position: center;
		//    background-size: 24px;
		//    background-image: svg-icon-upload($sec-dark-gray, $light-blue);
		//    text-align: center;
		//    content: '';
		//    width: $iconWidth;
		//    height: $iconHeight;
		//    top: 50%;
		//  }
		//}
		.uploader-screens {
			position: relative;
			box-sizing: border-box;
			opacity: 0;
			visibility: hidden;
			transform: translateX(20px);
			transition: 0.3s ease visibility, 0.3s ease opacity, 0.3s ease transform;
			.disable-animations & {
				transition-duration: 0s;
			}
			z-index: 2;
			padding: 0;
			&::after {
				display: table;
				clear: both;
				content: "";
			}
			.uploader-screen {
				display: none;
				&.active {
					display: block;
				}
			}
			ul {
				margin: 0;
				padding: 0;
				display: table;
				list-style-type: none;
				margin-bottom: 10px;
				li {
					display: table-row;
					margin: 0;
					padding: 0;
					.label {
						padding-right: 5px;
						font-weight: bold;
						display: table-cell;
						white-space: nowrap;
						width: 0;
						&::after {
							content: ":";
						}
					}
					.value {
						display: table-cell;
						word-break: break-all;
						a {
							display: inline-block;
						}
						&.error-message {
							color: $grapefruit-hover;
							font-size: inherit;
						}
					}
				}
			}
		}
		.uploader-progress-bar {
			position: absolute;
			top: 0;
			left: 0;
			height: 0;
			background-color: $pri-green;
			width: 0;
			transition: 0.3s cubic-bezier(0, 0, 0.5, 1) width, 0.3s ease height;
			.disable-animations & {
				transition-duration: 0s;
			}
			z-index: 1;
			border-top-left-radius: 1px;
			border-top-right-radius: 1px;
			&.uploader-progress-bar-show {
				height: 5px;
			}
		}
		&[data-upload-state="processing"] {
			.uploader-progress-bar {
				background-color: $blue;
			}
		}
		.uploader-thumbnail {
			width: $thumbnailSize;
			height: $thumbnailSize;
			background-size: contain;
			background-repeat: no-repeat;
			background-position: center center;
			background-color: $pri-gray;
		}
		&[data-compact="false"] {
			&::before {
				display: block;
				content: "";
				top: 25%;
				height: 50%;
				width: 1px;
				position: absolute;
				left: $iconWidth;
				background-color: $light-border-gray;
				transition: 0.3s ease transform;
				.disable-animations & {
					transition-duration: 0s;
				}
				transform: scaleY(0);
			}
			.uploader-screens {
				margin-left: $iconWidth;
				padding-left: $padding * 2;
			}
			.uploader-thumbnail-container {
				position: absolute;
				opacity: 0;
				visibility: hidden;
				right: $padding;
				transition: 0.3s ease opacity, 0.3s ease visibility;
				.disable-animations & {
					transition-duration: 0s;
				}
			}
			.uploader-show-thumbnail {
				.uploader-screens-info {
					margin-right: $thumbnailSize + $padding;
				}
				.uploader-thumbnail-container {
					opacity: 1;
					visibility: inherit;
				}
			}
		}
		&[data-compact="true"] {
			.uploader-thumbnail-container {
				height: 0;
				opacity: 0;
				transition: 0.3s ease opacity, 0.3s ease height;
				.disable-animations & {
					transition-duration: 0s;
				}
				overflow: hidden;
				width: $thumbnailSize;
				margin: 0 auto;
			}
			&.uploader-show-screens {
				.uploader-icon {
					opacity: 0;
				}
				.uploader-show-thumbnail {
					.uploader-thumbnail-container {
						opacity: 1;
						height: $thumbnailSize + $padding;
					}
				}
			}
		}
		&.uploader-show-screens {
			&::before {
				transform: scaleY(1);
			}
			.uploader-icon {
				left: 0;
				margin-left: 0;
			}
			.uploader-screens {
				opacity: 1;
				visibility: inherit;
				transform: none;
			}
			.uploader-dropzone {
				opacity: 0;
				visibility: hidden;
				transform: translateY(20px);
			}
		}
	}

</style>
