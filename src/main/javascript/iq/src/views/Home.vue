<template>
  
  <div class="home">
    <vue-single-upload class="file-upload" :show-details="showDetails"
                       @file-selected="fileDescriptor => fileSelected(fileDescriptor)" ref="singleUpload" @finished="onUploadFinished()"
                       :readonly="false" :compact="false" :base-url="'http://localhost:8080/api/ui/upload'">
      <div slot="details" :class="{'uploader-show-thumbnail' : uploadResult}" v-if="uploadResult">
        <div class="uploader-thumbnail-container" v-if="thumbnailUrl">
        
        </div>
        <ul>
          <li>
            <span class="label">{{$t('plugin.uploader.label.fileName')}}</span>
            <span class="value">{{fileName}}</span>
          </li>
          <li>
            <span class="label">{{$t('plugin.uploader.label.fileSize')}}</span>
            <span class="value">{{uploadResult.total | byte}}</span>
          </li>
        </ul>
        <div class="buttons">
          <button type="button" class="btn danger delete-button" @click="deleteFile()">
            {{$t('plugin.uploader.button.delete')}}
          </button>
        </div>
      </div>
    </vue-single-upload>
  
    <image-list class="mt-5" ref="imagelist"></image-list>
  
  </div>
  
</template>

<script lang="ts">
	import {Component, Prop, Vue} from 'vue-property-decorator';
	import VueSingleUpload, {UploadResult} from "@/components/VueSingleUpload.vue";
	import {IFileDescriptor} from "@ponte/file-upload-js";
	import ImageList from "@/components/ImageList.vue"; // @ is an alias to /src

	@Component({
  components: {
	  VueSingleUpload,
	  ImageList
  },
})
export default class Home extends Vue {

	@Prop() private msg!: string;
	@Prop() private showDetails!: boolean|undefined;

	public fileName: string | null = null;
	public thumbnailUrl: string | null = null;
	public uploadResult: UploadResult | null = null;

	private async fileSelected(fileDescriptor: IFileDescriptor) {
		this.fileName = fileDescriptor.file.name;
		this.uploadResult = await (<VueSingleUpload>this.$refs.singleUpload).uploadFile(fileDescriptor.file, { handler: 'ImageUploadHandler' });
	}
	
	private async onUploadFinished() {
		 (<ImageList>this.$refs.imagelist).refresh();
  }

	private deleteFile() {
		this.uploadResult = null;
	  (<VueSingleUpload>this.$refs.singleUpload).reset();
	}
	
}
</script>

<style lang="scss">
  .home {
    .uploader-dropzone {
      background-color: aliceblue;
    }
 
  }
 
</style>
