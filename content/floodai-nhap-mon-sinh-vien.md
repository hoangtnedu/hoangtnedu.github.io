# Học sâu từ CNN đến STGNN: hiểu các mô hình qua bài toán dự báo lũ

- Tác giả: Trần Nguyên Hoàng
- Ngày: 2026-11-04
- Đối tượng: Sinh viên CNTT mới làm quen với học sâu
- Bản HTML: https://hoangtnedu.github.io/2026/11/04/floodai-nhap-mon-cho-sinh-vien.html

> Tài liệu nhằm giải thích các kiến trúc và kỹ thuật trong hướng nghiên cứu FloodAI; ví dụ chỉ là giả định, chưa phải kết quả trên dữ liệu địa phương.

## Tổng quan

Có bốn nhóm đầu vào: chuỗi thời gian mưa/H/Q (TCN, GRU, LSTM, Transformer), ảnh radar/vệ tinh (CNN, ConvLSTM), đặc trưng tĩnh địa hình/đất (MLP), và đồ thị mạng sông có hướng (GNN/STGNN).

![Sơ đồ tổng thể](../../assets/figures/floodai-sinh-vien-ban-do.svg)

## 1. Mạng học sâu học như thế nào?

**Bắt đầu từ đầu vào, dự báo, sai số và lan truyền ngược.** Một mạng học sâu nhận dữ liệu đầu vào, biến đổi qua các lớp có tham số và đưa ra dự báo. Khi biết đáp án thật, ta tính loss (mức sai khác), dùng lan truyền ngược để tìm gradient và bộ tối ưu để cập nhật tham số. Sau nhiều vòng huấn luyện, mô hình học được quy luật từ dữ liệu.

**Ví dụ minh họa:** Giả sử đầu vào là lượng mưa 6 giờ gần nhất và mực nước hiện tại; đầu ra là mực nước dự báo sau 3 giờ. Mạng dự báo 3,6 m, giá trị đo sau đó là 4,0 m: sai số tuyệt đối bằng 0,4 m. Đây chỉ là ví dụ giả định.

![Sơ đồ 1. Mạng học sâu học như thế nào?](../../assets/figures/00-architecture.svg)

**Các bước:**
1. Chia dữ liệu theo sự kiện hoặc thời gian thành train, validation và test; tránh dùng tương lai để dự báo quá khứ.
2. Chuẩn hóa từ thống kê của tập train, sau đó tạo cửa sổ thời gian đầu vào.
3. Cho dữ liệu đi qua mạng để nhận giá trị dự báo (forward pass).
4. Tính loss, lan truyền gradient ngược rồi cập nhật trọng số bằng Adam hoặc SGD.
5. Dùng validation để chọn cấu hình/checkpoint; chỉ dùng test để đánh giá cuối cùng.

**Cần hiểu:** Loss là mục tiêu tối ưu trong huấn luyện; MAE, F1-score, NSE… là chỉ số dùng để báo cáo chất lượng. Hai khái niệm có liên quan nhưng không đồng nhất.

**Ghi nhớ:** Mô hình học từ các ví dụ, không tự biết quy luật thủy văn; chất lượng dữ liệu và thiết kế kiểm thử quan trọng không kém kiến trúc mạng.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-00

## 2. MLP — học từ đặc trưng tĩnh

**Multilayer Perceptron.** MLP là mạng truyền thẳng gồm các lớp Dense: mỗi lớp nhân vector với ma trận trọng số, cộng bias rồi áp dụng hàm phi tuyến như ReLU. MLP phù hợp với dữ liệu dạng bảng hoặc đặc trưng tĩnh, không tự mã hóa vị trí pixel hay thứ tự thời gian.

**Ví dụ minh họa:** Mỗi tiểu lưu vực có độ cao trung bình, độ dốc, diện tích, tỷ lệ rừng và loại đất. MLP biến các thuộc tính này thành một vector đặc trưng ẩn để đưa vào mô hình chung.

![Sơ đồ 2. MLP — học từ đặc trưng tĩnh](../../assets/figures/01-architecture.svg)

**Các bước:**
1. Tập hợp biến số thực và mã hóa biến phân loại (nếu có).
2. Chuẩn hóa các biến liên tục theo tập train.
3. Tính h = ReLU(Wx + b) tại từng lớp.
4. Lấy vector h cuối cùng làm biểu diễn tĩnh của nút/lưu vực.

**Cần hiểu:** Công thức cơ bản: h = φ(Wx + b). W và b được học; φ tạo phi tuyến.

**Ghi nhớ:** Không nên chỉ dùng MLP khi bài toán phụ thuộc mạnh vào trình tự mưa nhiều giờ hoặc quan hệ các trạm sông.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-01

## 3. CNN — nhìn ảnh radar bằng các bộ lọc

**Convolutional Neural Network.** CNN học các bộ lọc (kernel) trượt trên ảnh. Ở mỗi vị trí, kernel nhân với vùng nhỏ của ảnh rồi cộng lại để tạo bản đồ đặc trưng. Nhiều lớp CNN học từ mẫu cục bộ đơn giản đến cấu trúc không gian phức tạp hơn.

**Ví dụ minh họa:** Một ảnh radar mưa 64 × 64 ô lưới mô tả mưa tại khu vực nghiên cứu. CNN có thể học đặc trưng của dải mưa mạnh, vùng mưa liên tục và vị trí tâm mưa.

![Sơ đồ 3. CNN — nhìn ảnh radar bằng các bộ lọc](../../assets/figures/02-architecture.svg)

**Các bước:**
1. Nhận ảnh/lưới có dạng cao × rộng × kênh.
2. Quét kernel qua ảnh để tạo feature map.
3. Áp dụng hàm kích hoạt và có thể giảm kích thước không gian.
4. Dùng các feature map làm đầu vào cho head dự báo hoặc nhánh hợp nhất dữ liệu.

**Cần hiểu:** CNN học đặc trưng không gian, không tự động hiểu diễn biến theo thời gian nếu đầu vào chỉ là một ảnh.

**Ghi nhớ:** Ảnh mưa radar/vệ tinh là dữ liệu dạng lưới; khác với chuỗi mực nước tại một trạm.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-02

## 4. TCN — tích chập trên chuỗi thời gian

**Temporal Convolutional Network.** TCN dùng tích chập một chiều theo trục thời gian, thường với causal convolution (không nhìn trước tương lai) và dilated convolution (kernel lấy mẫu cách quãng). Nhờ dilation, mô hình quan sát được quá khứ dài hơn mà không cần bộ nhớ hồi tiếp.

**Ví dụ minh họa:** Chuỗi lượng mưa của 24 giờ gần nhất đi qua các lớp có dilation 1, 2, 4, 8 để học ảnh hưởng tích lũy và độ trễ của mưa lên mực nước.

![Sơ đồ 4. TCN — tích chập trên chuỗi thời gian](../../assets/figures/03-architecture.svg)

**Các bước:**
1. Biểu diễn cửa sổ quan trắc theo thứ tự thời gian.
2. Áp dụng tích chập nhân quả để đầu ra ở t chỉ phụ thuộc dữ liệu đến t.
3. Tăng dilation ở các lớp sâu, mở rộng receptive field.
4. Lấy các đặc trưng cuối để dự báo H/Q ở các horizon mong muốn.

**Cần hiểu:** Cần kiểm tra receptive field có bao trùm độ dài cửa sổ mong muốn hay không.

**Ghi nhớ:** TCN là baseline mạnh cho chuỗi; thiết kế nhân quả là điều kiện bắt buộc khi dự báo tương lai.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-03

## 5. GRU, LSTM và GRU-D — ghi nhớ chuỗi quan trắc

**Recurrent neural networks.** RNN xử lý từng thời điểm và truyền trạng thái ẩn sang bước kế tiếp. GRU dùng cổng cập nhật và cổng đặt lại; LSTM dùng thêm trạng thái ô nhớ và các cổng điều khiển để quản lý thông tin dài hạn. GRU-D mở rộng ý tưởng cho chuỗi có dữ liệu thiếu, dùng mask và khoảng thời gian từ quan trắc gần nhất.

**Ví dụ minh họa:** Mực nước có thể tăng chậm rồi dâng nhanh sau mưa lớn. LSTM/GRU học diễn biến quá khứ thay vì chỉ dùng giá trị tại một thời điểm; GRU-D có thể khai thác thông tin về việc cảm biến mất kết nối.

![Sơ đồ 5. GRU, LSTM và GRU-D — ghi nhớ chuỗi quan trắc](../../assets/figures/05-architecture.svg)

**Các bước:**
1. Đọc lần lượt các giá trị x₁, x₂, …, xₜ.
2. Tại mỗi bước, kết hợp xₜ và trạng thái trước bằng các cổng điều khiển.
3. Tạo trạng thái ẩn biểu diễn lịch sử đã đọc.
4. Đưa trạng thái ẩn đến bộ dự báo mực nước/lưu lượng.

**Cần hiểu:** LSTM không mặc nhiên tốt hơn GRU; cần cùng split, cùng horizon và nhiều lần chạy để so sánh công bằng.

**Ghi nhớ:** Các kiến trúc này giải quyết 'điều gì đã xảy ra trong quá khứ?', nhưng không tự khai thác đầy đủ hướng chảy giữa các trạm.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-05

## 6. ConvLSTM — vừa nhìn không gian, vừa theo thời gian

**Convolutional LSTM.** ConvLSTM giữ ý tưởng cổng nhớ của LSTM, nhưng thay các phép nhân tuyến tính trong cổng bằng tích chập. Vì thế trạng thái ẩn vẫn là bản đồ không gian, phù hợp với chuỗi ảnh radar mưa.

**Ví dụ minh họa:** Nhận 6 khung radar liên tiếp, mỗi khung 64 × 64, để học chuyển động của mưa và tín hiệu liên quan đến sự hình thành lũ.

![Sơ đồ 6. ConvLSTM — vừa nhìn không gian, vừa theo thời gian](../../assets/figures/07-architecture.svg)

**Các bước:**
1. Xếp ảnh radar thành chuỗi theo thời gian.
2. Tại mỗi bước, tính các cổng bằng tích chập trên ảnh hiện tại và trạng thái trước.
3. Cập nhật bản đồ nhớ và bản đồ trạng thái ẩn.
4. Đưa đặc trưng không gian–thời gian đến đầu ra dự báo.

**Cần hiểu:** ConvLSTM khác CNN đơn ảnh ở chỗ nó mô hình hóa sự thay đổi qua nhiều thời điểm.

**Ghi nhớ:** Dùng khi có ảnh/lưới liên tiếp; chất lượng đồng bộ thời gian và vị trí lưới rất quan trọng.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-07

## 7. Transformer và TFT — chú ý các thời điểm quan trọng

**Attention-based forecasting.** Self-attention giúp mỗi vị trí trong chuỗi tính mức liên quan tới các vị trí khác. Transformer kết hợp attention và các lớp biến đổi đặc trưng; để dự báo, cần giữ tính nhân quả hoặc chỉ đưa các biến tương lai thật sự biết trước. TFT (Temporal Fusion Transformer) là kiến trúc chuyên cho dự báo đa horizon, kết hợp lựa chọn biến, gating và attention.

**Ví dụ minh họa:** Khi dự báo mực nước 6 giờ tới, mô hình có thể chú ý đến đợt mưa lớn cách đây vài giờ thay vì coi mọi thời điểm trong cửa sổ đều có mức ảnh hưởng như nhau.

![Sơ đồ 7. Transformer và TFT — chú ý các thời điểm quan trọng](../../assets/figures/11-architecture.svg)

**Các bước:**
1. Biến chuỗi đầu vào thành các embedding theo thời điểm.
2. Bổ sung thông tin thứ tự/vị trí thời gian phù hợp.
3. Tính attention để trộn thông tin từ các thời điểm được phép dùng.
4. Giải mã ra nhiều thời hạn dự báo hoặc các phân vị.

**Cần hiểu:** Attention(Q,K,V) = softmax(QKᵀ/√dₖ) V. Trọng số attention không tự động là bằng chứng nhân quả.

**Ghi nhớ:** Cần cẩn thận không để mô hình nhìn trước lượng mưa hoặc số đo tương lai chưa thực sự có tại thời điểm phát dự báo.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-11

## 8. GNN, GCN, GraphSAGE và GAT — hiểu mạng sông

**Graph Neural Networks.** Đồ thị gồm nút (trạm/tiểu lưu vực) và cạnh (quan hệ dòng chảy). GNN học cách một nút nhận thông tin từ các nút lân cận. GCN dùng phép tổng hợp đã chuẩn hóa theo cấu trúc đồ thị; GraphSAGE dùng bộ gom đặc trưng lân cận; GAT học trọng số attention giữa các nút. Với mạng sông, hướng thượng lưu → hạ lưu phải được xác định rõ.

**Ví dụ minh họa:** Trạm A ở thượng lưu, B ở giữa, C ở hạ lưu: A → B → C. Nếu A ghi nhận mực nước tăng, một mô hình đồ thị có hướng có thể truyền đặc trưng về phía B rồi C.

![Sơ đồ 8. GNN, GCN, GraphSAGE và GAT — hiểu mạng sông](../../assets/figures/10-architecture.svg)

**Các bước:**
1. Tạo danh sách các trạm/nút và cạnh có hướng hợp lệ.
2. Gắn cho mỗi nút chuỗi mưa, mực nước, lưu lượng hoặc embedding.
3. Tính thông điệp từ nút liên quan theo quy tắc của GCN/GraphSAGE/GAT.
4. Gom thông điệp, cập nhật embedding nút và chuyển đến bộ dự báo.

**Cần hiểu:** GNN là một họ kiến trúc, không phải một thuật toán duy nhất; cần thiết kế cạnh phù hợp ý nghĩa vật lý và tránh truyền dữ liệu tương lai.

**Ghi nhớ:** Graph mô tả 'nước liên quan giữa các vị trí nào', nhưng chỉ GNN thuần chưa đủ biểu diễn biến động theo thời gian.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-10

## 9. STGNN — ghép thời gian với mạng sông

**Spatio-Temporal Graph Neural Network.** STGNN kết hợp một khối học thời gian (TCN/GRU/Transformer…) với một khối lan truyền theo đồ thị (GCN/GAT/message passing). Mô hình vừa đọc lịch sử ở từng nút, vừa học sự phụ thuộc giữa các nút liên kết.

**Ví dụ minh họa:** Mỗi trạm có 24 giờ lịch sử và các thuộc tính lưu vực. Đồ thị nối trạm thượng lưu tới hạ lưu. Đầu ra có thể là mực nước tại trạm hạ lưu sau 1, 3, 6, 12 giờ.

![Sơ đồ 9. STGNN — ghép thời gian với mạng sông](../../assets/figures/13-architecture.svg)

**Các bước:**
1. Mã hóa lịch sử tại từng trạm bằng bộ học chuỗi.
2. Trao đổi embedding dọc các cạnh mạng sông có hướng.
3. Trộn biểu diễn không gian và thời gian theo kiến trúc đã chọn.
4. Dùng decoder đa horizon để phát dự báo cho các trạm đích.

**Cần hiểu:** STGNN không đồng nghĩa GNN có hướng; 'spatio-temporal' nhấn mạnh sự kết hợp giữa hai trục quan hệ.

**Ghi nhớ:** Đây là phần cốt lõi giúp mô hình nghiên cứu thể hiện đường truyền lũ từ thượng lưu xuống hạ lưu.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-13

## 10. Hợp nhất đa nguồn thích nghi — nguồn nào đáng tin hơn?

**Quality-aware adaptive fusion.** Trạm mưa, trạm mực nước, radar, vệ tinh và dữ liệu tĩnh có kiểu dữ liệu khác nhau. Mỗi nguồn cần encoder phù hợp. Sau đó một cổng hợp nhất (gate) tính trọng số dựa vào đặc trưng, mask có/thiếu, age (độ trễ cập nhật) và quality flag. Các trọng số được chuẩn hóa, ví dụ bằng masked softmax, rồi cộng các biểu diễn nguồn.

**Ví dụ minh họa:** Ví dụ ba nguồn có trọng số hợp lệ 0,60 (mưa trạm), 0,30 (radar) và 0,10 (vệ tinh). Nếu radar mất nguồn, cổng phải bỏ radar khỏi tập nguồn khả dụng và phân phối lại trọng số cho các nguồn còn lại; số này chỉ minh họa.

![Sơ đồ 10. Hợp nhất đa nguồn thích nghi — nguồn nào đáng tin hơn?](../../assets/figures/14-architecture.svg)

**Các bước:**
1. Mã hóa từng nguồn với encoder thích hợp (MLP/CNN/TCN…).
2. Đưa kèm mask, tuổi dữ liệu, chất lượng và ngữ cảnh vào gate.
3. Dùng masked softmax trên các nguồn có sẵn để có trọng số tổng bằng 1.
4. Tính vector tổng hợp z = Σ αₛ hₛ; kiểm tra nguy cơ gate collapse.
5. Đưa z vào mô hình thời gian–đồ thị và các head dự báo.

**Cần hiểu:** Softmax chỉ tạo ra trọng số tương đối. Nếu nguồn mất hoàn toàn, cần mask rõ ràng; softmax đơn thuần không tự biết nguồn không khả dụng.

**Ghi nhớ:** Đây là hướng mô hình đề xuất cần thực nghiệm kiểm chứng, không phải kết quả đã được chứng minh trên dữ liệu Lạng Sơn.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-14

## 11. Học đa nhiệm — vừa dự báo mực nước, vừa cảnh báo

**Multi-task learning.** Một backbone học biểu diễn chung rồi tách thành nhiều head. Head hồi quy dự báo Q/H; head phân loại đưa ra xác suất vượt ngưỡng; head phân vị hoặc khoảng dự báo mô tả bất định. Huấn luyện bằng tổng có trọng số của các loss theo nhiệm vụ và theo horizon.

**Ví dụ minh họa:** Ở trạm mục tiêu sau 3 giờ: dự báo H = 4,7 m, xác suất vượt mức 4,5 m là 0,82; khoảng dự báo ví dụ [4,2; 5,1] m. Các số đều là giả định minh họa.

![Sơ đồ 11. Học đa nhiệm — vừa dự báo mực nước, vừa cảnh báo](../../assets/figures/14-flow.svg)

**Các bước:**
1. Dùng encoder/fusion/STGNN tạo embedding chung.
2. Head 1 dự báo Q và H tại từng horizon.
3. Head 2 học xác suất vượt ngưỡng dựa trên nhãn sự kiện.
4. Head 3 học phân vị hoặc khoảng dự báo; ghép các loss với hệ số hợp lý.
5. Đánh giá riêng từng head thay vì dùng một độ chính xác chung.

**Cần hiểu:** Một xác suất 0,82 không có nghĩa 'chắc chắn xảy ra lũ'; cần kiểm định chất lượng xác suất bằng Brier score và calibration curve.

**Ghi nhớ:** Phải phân biệt Q/H (đại lượng liên tục) với cảnh báo vượt ngưỡng (sự kiện nhị phân hoặc nhiều cấp).

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-14

## 12. Dữ liệu thiếu và huấn luyện bền vững

**Mask-aware · Source dropout · Consistency training.** Ngoài giá trị đo, mô hình có thể nhận mask (có dữ liệu hay không), age (đã bao lâu chưa cập nhật) và quality flag (chất lượng). Source dropout chủ động tắt ngẫu nhiên một nguồn trong huấn luyện; consistency training khuyến khích dự báo từ bản đủ nguồn và bản thiếu nguồn không khác nhau quá mức khi điều kiện cho phép. Robust loss giảm ảnh hưởng của sai số ngoại lai.

**Ví dụ minh họa:** Nếu trạm B mất 3 giờ liên tục, ghi mask=0 cho các quan trắc thiếu và tăng age; không nên tự gán 0 mm mưa hay 0 m mực nước nếu đó không phải số đo thật.

![Sơ đồ 12. Dữ liệu thiếu và huấn luyện bền vững](../../assets/figures/24-architecture.svg)

**Các bước:**
1. Giữ giá trị gốc và tạo mask chỉ rõ vị trí thiếu.
2. Tính age từ thời điểm có quan trắc hợp lệ gần nhất.
3. Tạo các kịch bản missing block, sensor dropout, noise và delay.
4. Huấn luyện model với mask-aware encoder và loss thích hợp.
5. So sánh hiệu năng khi đầy đủ nguồn và khi mất 10–50% dữ liệu trên cùng test.

**Cần hiểu:** Source dropout là kỹ thuật huấn luyện, khác dropout thông thường ở cấp neuron. Không dùng nhãn tương lai để tự điền giá trị thiếu tại thời điểm dự báo.

**Ghi nhớ:** Độ bền vững là khả năng suy giảm ít khi dữ liệu xấu đi; không thể suy ra chỉ từ MAE lúc dữ liệu đầy đủ.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-24

## 13. Bất định — dự báo chắc đến mức nào?

**Quantile · Ensemble · MC dropout · Conformal.** Dự báo một số duy nhất chưa phản ánh độ tin cậy. Quantile regression tạo các phân vị (ví dụ 5% và 95%); mixture density network học phân phối có thể gồm nhiều thành phần; deep ensemble và Monte Carlo dropout phản ánh một phần bất định do mô hình; conformal prediction hiệu chỉnh khoảng dự báo bằng dữ liệu calibration riêng, dưới các giả định phù hợp.

**Ví dụ minh họa:** Mô hình dự báo H sau 6 giờ là 4,9 m, khoảng dự báo danh định 90% là [4,2; 5,8] m. Cần đo coverage trên tập kiểm thử độc lập, không thể chỉ nhìn độ rộng khoảng để kết luận tốt.

![Sơ đồ 13. Bất định — dự báo chắc đến mức nào?](../../assets/figures/19-architecture.svg)

**Các bước:**
1. Chọn cách dự báo phân vị/phân phối hoặc tập hợp mô hình.
2. Tách calibration set khỏi train và test nếu dùng conformal.
3. Tạo khoảng dự báo cho từng lead time và ngưỡng cảnh báo.
4. Đo coverage cùng interval width; với xác suất vượt ngưỡng đo Brier và calibration.
5. Kiểm tra riêng các sự kiện cực đoan và các giai đoạn đỉnh lũ.

**Cần hiểu:** Coverage cao bằng cách mở rộng khoảng vô hạn là vô ích; cần cân bằng độ phủ và độ sắc nét.

**Ghi nhớ:** Độ tin cậy vận hành là chủ đề cần hiệu chuẩn và kiểm chứng, không đồng nghĩa độ chính xác trung bình cao.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-19

## 14. Học chuyển giao và tri thức vật lý

**Transfer learning · Physics-guided learning.** Khi dữ liệu Lạng Sơn hạn chế, có thể pretrain trên dữ liệu mở đa lưu vực rồi fine-tune với dữ liệu địa phương. Tri thức thủy văn còn giúp đưa các ràng buộc vào mô hình: hướng chảy trên sông, giá trị lưu lượng/mực nước có miền hợp lệ, trật tự các ngưỡng cảnh báo và nhất quán vật lý ở mức phù hợp.

**Ví dụ minh họa:** Pretrain encoder trên CAMELS/LamaH-CE để học quan hệ mưa–dòng chảy, rồi fine-tune trên trạm Lạng Sơn; so với mô hình train from scratch bằng cùng dữ liệu và cùng split.

![Sơ đồ 14. Học chuyển giao và tri thức vật lý](../../assets/figures/22-architecture.svg)

**Các bước:**
1. Chọn nguồn dữ liệu mở tương thích về biến, đơn vị và độ phân giải.
2. Pretrain trên tập nguồn, giữ riêng các tập kiểm thử độc lập.
3. Fine-tune một phần hoặc toàn bộ encoder trên dữ liệu địa phương.
4. So sánh với train from scratch và frozen encoder.
5. Bổ sung ràng buộc vật lý hợp lý, rồi chạy ablation để kiểm chứng đóng góp.

**Cần hiểu:** Không đưa dữ liệu của sự kiện kiểm thử địa phương vào pretrain/fine-tune. Khả năng chuyển giao là giả thuyết cần chứng minh.

**Ghi nhớ:** Mục tiêu là đóng góp thuật toán học sâu, không phải xây dựng toàn bộ phần mềm GIS, cảm biến IoT hoặc mô hình thủy lực từ đầu.

**Xem sâu hơn:** https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html#model-22

## Đánh giá mô hình

| Chỉ số | Công thức tóm tắt | Ý nghĩa |
|---|---|---|
| Accuracy | (TP + TN) / (TP + TN + FP + FN) | Đúng trên toàn bộ mẫu; dễ lạc quan khi lớp không lũ chiếm đa số. |
| Precision | TP / (TP + FP) | Trong các cảnh báo lũ, bao nhiêu cảnh báo đúng? |
| Recall / POD | TP / (TP + FN) | Trong tất cả trường hợp thực sự có lũ, phát hiện được bao nhiêu? |
| F1-score | 2 × Precision × Recall / (Precision + Recall) | Trung bình điều hòa giữa Precision và Recall. |
| FAR (False Alarm Ratio) | FP / (TP + FP) | Bao nhiêu cảnh báo phát ra là báo động giả? Khác FPR. |
| CSI / IoU nhị phân | TP / (TP + FP + FN) | Đo giao/hợp giữa tập sự kiện có lũ dự báo và thực tế. |
| MAE | mean(/y − ŷ/) | Trung bình sai bao nhiêu mét mực nước hay m³/s lưu lượng? |
| RMSE | sqrt(mean((y − ŷ)²)) | Nhạy hơn với sai số lớn, đặc biệt gần đỉnh lũ. |
| NSE | 1 − Σ(y − ŷ)² / Σ(y − ȳ)² | So với dự báo hằng bằng trung bình quan trắc; 1 là hoàn hảo. |
| KGE | 1 − √[(r − 1)² + (α − 1)² + (β − 1)²] | Xét tương quan r, tỷ lệ độ lệch chuẩn α, tỷ lệ trung bình β. |
| Brier score | mean((p − y)²) | Chất lượng xác suất dự báo sự kiện; càng thấp càng tốt. |
| Coverage + Interval width | Tỷ lệ nằm trong khoảng + độ rộng trung bình | Đánh giá đồng thời độ phủ và độ sắc nét của khoảng dự báo. |

### Ví dụ ma trận nhầm lẫn

TP=40, TN=920, FP=30, FN=10 (trên 1000 trường hợp). Accuracy=96%; Precision≈57,14%; Recall=80%; F1≈66,67%; FAR≈42,86%.

## Quy trình thực nghiệm

Chia train/validation/test trước khi tạo sliding windows để giảm rò rỉ sự kiện; đánh giá theo từng horizon 1/3/6/12 giờ, nhiều seed, ablation và kịch bản thiếu dữ liệu; kiểm chứng độc lập Lạng Sơn.

![Minh họa mask và age](../../assets/figures/floodai-sinh-vien-mask.svg)

## Câu hỏi ôn tập

**Câu 1. Vì sao Accuracy = 96% chưa đủ để kết luận một hệ thống cảnh báo lũ là tốt?**

Đáp án: Vì dữ liệu có thể mất cân bằng. Cần biết bao nhiêu trận lũ bị bỏ sót (FN) và bao nhiêu báo động giả (FP), bằng Recall/POD, FAR, F1, CSI.

**Câu 2. Radar mưa dạng ảnh và mực nước dạng chuỗi nên đi qua encoder nào?**

Đáp án: Ảnh radar có thể dùng CNN hoặc ConvLSTM nếu là chuỗi ảnh; mực nước dạng chuỗi có thể dùng TCN, GRU, LSTM hoặc Transformer. Sau đó mới hợp nhất biểu diễn.

**Câu 3. Trạm đo thiếu ba giờ liên tiếp: tại sao không tự điền giá trị 0?**

Đáp án: Vì 0 là một số đo có nghĩa vật lý, còn không có quan trắc là tình trạng khác. Cần mask, age và chiến lược xử lý thiếu phù hợp.

**Câu 4. Trạm A thượng lưu ảnh hưởng B hạ lưu; GNN và STGNN khác nhau thế nào?**

Đáp án: GNN xử lý quan hệ giữa các nút trên đồ thị; STGNN đồng thời mô hình hóa diễn biến theo thời gian. Với mạng sông cần kiểm tra chiều truyền thông tin A → B.

**Câu 5. TP=40, FP=30, FN=10. Tính Precision, Recall và F1.**

Đáp án: Precision = 40/70 ≈ 57,14%; Recall = 40/50 = 80%; F1 = 2×40/(2×40+30+10) ≈ 66,67%.

**Câu 6. Khoảng dự báo 90% bao phủ 98% số đo thực tế có chắc tốt không?**

Đáp án: Chưa chắc: khoảng có thể quá rộng. Cần xem đồng thời empirical coverage, interval width, điểm interval score và hiệu chuẩn theo horizon.

**Câu 7. Chuyển giao từ dữ liệu mở sang Lạng Sơn cần lưu ý gì?**

Đáp án: Dữ liệu nguồn và đích phải chuẩn hóa phù hợp; không được rò rỉ dữ liệu kiểm thử địa phương vào pretrain/fine-tune, và phải đánh giá độc lập trên sự kiện địa phương.

**Câu 8. Vì sao không được tạo sliding windows rồi chia train/test ngẫu nhiên?**

Đáp án: Các cửa sổ chồng lấn của cùng một trận lũ có thể xuất hiện ở cả train và test. Cần chia trước theo sự kiện và thời gian để giảm rò rỉ thông tin.


## Nguồn đọc thêm

- FloodAI chuyên sâu: https://hoangtnedu.github.io/2026/10/27/floodai-mo-hinh-hoc-sau.html
- Dive into Deep Learning: https://d2l.ai/
- ConvLSTM: https://arxiv.org/abs/1506.04214
- GCN: https://arxiv.org/abs/1609.02907
- GraphSAGE: https://arxiv.org/abs/1706.02216
- GAT: https://arxiv.org/abs/1710.10903
- Transformer: https://arxiv.org/abs/1706.03762
- TFT: https://arxiv.org/abs/1912.09363
- GRU-D: https://arxiv.org/abs/1606.01865
