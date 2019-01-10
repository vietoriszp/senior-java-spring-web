<template>
  
  <div class="uploader-dropzone">
    <div class="uploader-dropzone-label" v-if="allowDirectorySelection">
      {{$t('plugin.uploader.selectfile.modern.directory')}}
    </div>
    <!--<div class="uploader-dropzone-label" v-if="!allowDirectorySelection">-->
      <!--{{$t('plugin.uploader.selectfile.modern.simple')}}-->
    <!--</div>-->
    <div class="uploader-icon"></div>
  </div>
  
</template>

<script lang="ts">
  import { FileSelector } from '@ponte/file-upload-js';
  import { Component, Prop, Vue } from 'vue-property-decorator';

	export const FILE_SELECTED_EVENT = 'file-selected';
	export const ERROR_EVENT = 'error';

	@Component
	export default class VueFileSelector extends Vue {
	  @Prop() private allowDirectorySelection!: boolean| undefined;

		private fileSelector: FileSelector | undefined;
		private fileSelectorElement: HTMLDivElement | undefined;

	  protected mounted() {
	  
			this.fileSelectorElement = document.createElement('div');
			this.fileSelectorElement.className = 'file-selector';
			
			this.$el.appendChild(this.fileSelectorElement);
			
			this.fileSelector = new FileSelector({
				fileSelected: (fileDescriptor) => this.$emit(FILE_SELECTED_EVENT, fileDescriptor),
				element: this.fileSelectorElement,
				allowDirectorySelection: this.allowDirectorySelection,
				errorCallback: (errorMessageKey) => this.$emit(ERROR_EVENT, errorMessageKey)
			});

		}

	  public beforeDestroy() {

			if (this.fileSelectorElement) {
				this.$el.removeChild(this.fileSelectorElement);
				this.fileSelectorElement = undefined;
			}
			if (this.fileSelector) {
				this.fileSelector = undefined;
			}
		}

	}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  lang="scss">
  
  @function str-replace($string, $search, $replace: '') {
    $index: str-index($string, $search);
    
    @if $index {
      @return str-slice($string, 1, $index - 1) + $replace + str-replace(str-slice($string, $index + str-length($search)), $search, $replace);
    }
    
    @return $string;
  }
  
  @function data-uri-escape($str) {
    $str: str-replace($str, "%", "%25");
    $str: str-replace($str, ">", "%3E");
    $str: str-replace($str, "<", "%3C");
    $str: str-replace($str, "#", "%23");
    $str: str-replace($str, '"', "%22");
    @return $str;
  }
  
  
  @function data-svg-uri($str) {
    @return "data:image/svg+xml," + data-uri-escape($str);
  }
  
  @function svg-icon-upload($color, $color2) {
    @return url(data-svg-uri('<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path fill="#{$color}" d="M22,13v5H2V13H1v6a1,1,0,0,0,1,1H22a1,1,0,0,0,1-1V13Z"/><path fill="#{$color2}" d="M17.39,9.19,11.84,4.1A.41.41,0,0,0,11.53,4h-.06a.41.41,0,0,0-.3.09L5.61,9.19a.43.43,0,0,0,.08.64.53.53,0,0,0,.69.07L11,5.67V14.5a.5.5,0,0,0,1,0V5.66L16.62,9.9a.53.53,0,0,0,.69-.07A.43.43,0,0,0,17.39,9.19Z"/></svg>'));
  }
  
  
  $iconHeight: 40px;
  $iconWidth: 70px;
  $trd-green: #68CEA2;
  $sec-dark-gray: #555;
  $light-blue: #378caf;
  
  .uploader-dropzone {
    overflow: hidden;
    position: relative;
    text-align: center;
    height: 100px;
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
      z-index: 3;
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
  
  .uploader-icon {
    position: absolute;
    z-index: 0;
    left: 50%;
    top: 50%;
    margin-left: -$iconWidth/2;
    margin-top: -$iconHeight/2;
    height: $iconHeight;
    &::before {
      display: block;
      line-height: $iconHeight;
      background-repeat: no-repeat;
      background-position: center;
      background-size: 24px;
      background-image: svg-icon-upload($sec-dark-gray, $light-blue);
      text-align: center;
      content: '';
      width: $iconWidth;
      height: $iconHeight;
      top: 50%;
    }
  }
</style>
