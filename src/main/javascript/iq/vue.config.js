module.exports = {
	publicPath:	process.env.APP_MODE === 'production' ? '/ui/' : '/',
	outputDir: "../../../../target/classes/javascript/iq/",
	devServer: {
		port: 9000
	}
};