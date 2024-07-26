import axios from 'axios'

const api = axios.create({ //기본 요청 주소
	baseURL: 'http://localhost:8080/api', 
	//baseURL: 'http://13.125.161.122:8080/api',
	headers: {
		"Content-Type": "application/json",
		withCredentials: true
	}
});

api.interceptors.request.use(config => {   //맨처음 요청 보내는얘
	const token = localStorage.getItem('userInfo');
	if (token) {
		config.headers.Authorization = `Bearer ${token}`;
	}
	return config;
}, error => Promise.reject(error));

api.interceptors.response.use( //맨처음 요청에서 오류나면 실행되는 얘
	response => response,
	async (error) => {
		const originalRequest = error.config;
		if (error.response.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true; // 재시도 플래그를 설정하여 무한 반복 방지
			try {
				// '/api/refresh' 엔드포인트를 호출하여 새 액세스 토큰을 요청
				const { data } = await axios.post('http://localhost:8080/api/refresh', {}, { withCredentials: true });
				//const { data } = await axios.post('http://13.125.161.122:8080/api/refresh', {}, { withCredentials: true });
				localStorage.setItem('userInfo', data);
				// 오리지널 요청에 새 토큰을 설정하고 요청을 다시 시도
				originalRequest.headers['Authorization'] = `Bearer ${data}`;
				return api(originalRequest);
			} catch (refreshError) {
				console.log('refreshError', refreshError);
				try {
					axios.get('http://localhost:8080/api/Logout', {withCredentials: true});
					//axios.get('http://13.125.161.122:8080/api/Logout', {}, { withCredentials: true });
					localStorage.removeItem('userInfo'); // 세션 스토리지에서 사용자 정보 제거
					alert("다시 로그인해주세요.");
					window.location.reload();
				} catch (error) {
					alert('다시 시도해주세요.');
				}
				return Promise.reject(refreshError);
			}
		}
		return Promise.reject(error);
	}
);
export default api;